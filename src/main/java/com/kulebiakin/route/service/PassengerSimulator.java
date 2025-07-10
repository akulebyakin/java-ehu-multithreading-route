package com.kulebiakin.route.service;

import com.kulebiakin.route.model.BusStop;
import com.kulebiakin.route.model.Passenger;
import com.kulebiakin.route.state.StopState;
import com.kulebiakin.route.state.impl.BusyState;
import com.kulebiakin.route.state.impl.IdleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class PassengerSimulator extends Thread {
    private static final Logger log = LoggerFactory.getLogger(PassengerSimulator.class);
    private static final String[] PASSENGER_NAMES = {"Andrey", "Anna", "Marat", "Julia", "Olga"};

    @Override
    public void run() {
        log.info("PassengerSimulator started");
        Collection<BusStop> stops = BusStopManager.getInstance().getAllStops();

        log.info("Found {} stops to simulate passengers", stops.size());

        try {
            TimeUnit.SECONDS.sleep(3); // wait for buses to arrive
        } catch (InterruptedException e) {
            log.warn("Initial sleep interrupted in PassengerSimulator");
            return;
        }

        for (int i = 0; i < 10; i++) {
            for (BusStop stop : stops) {
                Passenger passenger = new Passenger(PASSENGER_NAMES[i % PASSENGER_NAMES.length] + "-" + i);
                passenger.actAtStop(stop);

                int busCount = stop.getBusCount();
                StopState state = (busCount > 0) ? new BusyState() : new IdleState();
                state.handle(stop);

                log.info("Stop {}: {} buses currently", stop.getBusStopName(), busCount);
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.warn("PassengerSimulator interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("PassengerSimulator completed");
    }
}
