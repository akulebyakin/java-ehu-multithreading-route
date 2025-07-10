package com.kulebiakin.route;

import com.kulebiakin.route.model.Bus;
import com.kulebiakin.route.reader.RouteInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class RouteApplication {

    private static final String ROUTE_FILE = "routes.txt";
    private static final Logger log = LoggerFactory.getLogger(RouteApplication.class);

    public static void main(String[] args) throws URISyntaxException {

        RouteInitializer initializer = new RouteInitializer();
        List<Bus> buses = initializer.loadBuses(Paths.get(Objects.requireNonNull(RouteApplication.class.getClassLoader().getResource(ROUTE_FILE)).toURI()));

        log.info("Total buses loaded: {}", buses.size());

        // Start all bus threads
        for (Bus bus : buses) {
            bus.start();
        }

        // Wait for all threads to finish
        for (Bus bus : buses) {
            try {
                bus.join();
            } catch (InterruptedException e) {
                log.error("Main thread was interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        log.info("The route system has finished working.");
    }
}
