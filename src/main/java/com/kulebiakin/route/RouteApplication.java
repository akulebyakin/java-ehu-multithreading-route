package com.kulebiakin.route;

import com.kulebiakin.route.model.Bus;
import com.kulebiakin.route.reader.RouteLoader;
import com.kulebiakin.route.service.PassengerSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RouteApplication {

    private static final String ROUTE_FILE = "routes.txt";
    private static final Logger log = LoggerFactory.getLogger(RouteApplication.class);

    public static void main(String[] args) {
        RouteLoader initializer = new RouteLoader();
        List<Bus> buses = initializer.loadBuses(getRouteFilePath(ROUTE_FILE));
        log.info("Total buses loaded: {}", buses.size());

        if (buses.isEmpty()) {
            log.error("No buses found in the route file. Exiting.");
            return;
        }

        // Start passenger simulation thread
        new PassengerSimulator().start();

        // Start all bus threads
        for (Bus bus : buses) {
            bus.start();
        }

        // Wait for all bus threads to complete
        for (Bus bus : buses) {
            try {
                bus.join();
            } catch (InterruptedException e) {
                log.warn("Main thread was interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        log.info("Bus route simulation completed");
    }

    private static Path getRouteFilePath(String routeFile) {
        try {
            return Path.of(Objects.requireNonNull(
                RouteApplication.class.getClassLoader().getResource(routeFile),
                "Route file not found: " + routeFile
            ).toURI());
        } catch (URISyntaxException e) {
            log.error("Failed to resolve route file: {}", routeFile, e);
            throw new RuntimeException(e);
        }
    }
}
