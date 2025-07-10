package com.kulebiakin.route.reader;

import com.kulebiakin.route.model.Bus;
import com.kulebiakin.route.model.BusStop;
import com.kulebiakin.route.service.BusStopManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RouteLoader {
    private static final Logger log = LoggerFactory.getLogger(RouteLoader.class);
    private static final String COLUMN_SEPARATOR = ";";
    private static final String STOP_NAME_SEPARATOR = ",";

    public List<Bus> loadBuses(Path filePath) {
        List<Bus> buses = new ArrayList<>();

        try (Stream<String> lines = Files.lines(filePath)) {
            lines.filter(line -> !line.isBlank() && !line.startsWith("#"))
                .forEach(line -> {
                    try {
                        String[] parts = line.split(COLUMN_SEPARATOR);
                        String busName = parts[0].trim();
                        int waitTime = Integer.parseInt(parts[1].trim());

                        List<BusStop> route = new ArrayList<>();
                        for (String stopName : parts[2].split(STOP_NAME_SEPARATOR)) {
                            route.add(BusStopManager.getInstance().getOrCreateStop(stopName.trim()));
                        }

                        buses.add(new Bus(busName, route, waitTime));
                        log.info("Created bus {} with route {}", busName, parts[2]);
                    } catch (Exception e) {
                        log.warn("Error reading line '{}', skipping", line, e);
                    }
                });

        } catch (IOException e) {
            log.error("Failed to load routes from file: {}", e.getMessage());
        }

        return buses;
    }
}
