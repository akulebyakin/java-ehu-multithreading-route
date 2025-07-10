package com.kulebiakin.route.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Bus extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Bus.class);

    private final List<BusStop> route;
    private final int waitSecondsPerStop;

    public Bus(String name, List<BusStop> route, int waitSecondsPerStop) {
        super(name);
        this.route = route;
        this.waitSecondsPerStop = waitSecondsPerStop;
    }

    @Override
    public void run() {
        for (BusStop stop : route) {
            try {
                stop.arrive(this);

                log.info("{} is at stop {} ({} sec)", getName(), stop.getBusStopName(), waitSecondsPerStop);
                TimeUnit.SECONDS.sleep(waitSecondsPerStop);

                stop.depart(this);
            } catch (InterruptedException e) {
                log.warn("{} was interrupted on the route: {}", getName(), e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.info("{} has completed the route.", getName());
    }
}
