package com.kulebiakin.route.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Passenger {
    private static final Logger log = LoggerFactory.getLogger(Passenger.class);

    private final String name;

    public Passenger(String name) {
        this.name = name;
    }

    public void actAtStop(BusStop stop) {
        int action = ThreadLocalRandom.current().nextInt(2);
        if (action == 0) {
            log.info("Passenger {} stays at stop {}", name, stop.getBusStopName());
        } else {
            log.info("Passenger {} transfers to another bus at stop {}", name, stop.getBusStopName());
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Passenger passenger = (Passenger) o;
        return Objects.equals(name, passenger.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
