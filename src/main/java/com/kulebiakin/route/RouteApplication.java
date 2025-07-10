package com.kulebiakin.route;

import com.kulebiakin.route.model.Bus;
import com.kulebiakin.route.model.BusStop;
import com.kulebiakin.route.model.Passenger;
import com.kulebiakin.route.reader.RouteInitializer;
import com.kulebiakin.route.service.BusStopManager;
import com.kulebiakin.route.state.StopState;
import com.kulebiakin.route.state.impl.BusyState;
import com.kulebiakin.route.state.impl.IdleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RouteApplication {

    private static final String ROUTE_FILE = "routes.txt";
    private static final Logger log = LoggerFactory.getLogger(RouteApplication.class);

    public static void main(String[] args) {
        RouteInitializer initializer = new RouteInitializer();
        List<Bus> buses = initializer.loadBuses(getRouteFilePath(ROUTE_FILE));
        log.info("Total buses loaded: {}", buses.size());

        // Start bus threads
        for (Bus bus : buses) {
            bus.start();
        }

        // Simulate passengers at stops
        simulatePassengers();

        // Wait for bus threads to finish
        for (Bus bus : buses) {
            try {
                bus.join();
            } catch (InterruptedException e) {
                log.warn("Main thread was interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        log.info("Bus route simulation completed.");
    }

    private static Path getRouteFilePath(String routeFile) {
        try {
            return Path.of(Objects.requireNonNull(
                RouteApplication.class.getClassLoader().getResource(routeFile),
                "Route file not found: " + routeFile
            ).toURI());
        } catch (URISyntaxException e) {
            log.error("Route file not found: " + routeFile, e);
            throw new RuntimeException(e);
        }
    }

    private static void simulatePassengers() {
        new Thread(() -> {
            BusStopManager manager = BusStopManager.getInstance();
            String[] passengerNames = {"Andrey", "Svetlana", "Marat", "Yulia", "Olga"};

            for (int i = 0; i < 5; i++) {
                for (BusStop stop : manager.getAllStops()) {
                    Passenger p = new Passenger(passengerNames[i % passengerNames.length] + "-" + i);
                    p.actAtStop(stop);

                    // You can call the stop state
                    StopState state = stopStateByBusCount(stop);
                    state.handle(stop);
                }

                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    log.warn("Passenger thread interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "PassengerSimulation").start();
    }

    private static StopState stopStateByBusCount(BusStop stop) {
        // Simple logic — if occupancy > 0, consider BUSY
        return stop.getBusCount() > 0 ? new BusyState() : new IdleState();
    }
}
