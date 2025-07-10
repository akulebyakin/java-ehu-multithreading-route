package com.kulebiakin.route.reader;

import com.kulebiakin.route.model.Bus;
import com.kulebiakin.route.model.BusStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RouteInitializer {
    private static final Logger log = LoggerFactory.getLogger(RouteInitializer.class);

    private final Map<String, BusStop> stopRegistry = new HashMap<>();

    public List<Bus> loadBuses(Path filePath) {
        List<Bus> buses = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(";");
                String busName = parts[0].trim();
                int waitTime = Integer.parseInt(parts[1].trim());

                String[] stopNames = parts[2].split(",");
                List<BusStop> route = new ArrayList<>();

                for (String stopName : stopNames) {
                    stopName = stopName.trim();
                    BusStop stop = stopRegistry.computeIfAbsent(stopName, name -> new BusStop(name, 2));
                    route.add(stop);
                }

                Bus bus = new Bus(busName, route, waitTime);
                buses.add(bus);

                log.info("Created bus {} with route {}", busName, stopNames);
            }
        } catch (IOException e) {
            log.error("Error reading routes: {}", e.getMessage());
        }

        return buses;
    }

    public Collection<BusStop> getAllStops() {
        return stopRegistry.values();
    }
}
