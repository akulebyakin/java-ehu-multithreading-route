package com.kulebiakin.route.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BusStop {
    private static final Logger log = LoggerFactory.getLogger(BusStop.class);

    private final String busStopName;
    private final int maxBuses;
    private final Lock lock = new ReentrantLock();
    private final Condition spaceAvailable = lock.newCondition();
    private final List<Bus> currentBuses = new ArrayList<>();

    public BusStop(String busStopName, int maxBuses) {
        this.busStopName = busStopName;

        this.maxBuses = maxBuses;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void arrive(Bus bus) throws InterruptedException {
        lock.lock();
        try {
            while (currentBuses.size() >= maxBuses) {
                log.info("{} is waiting for entrance to the bus stop {}", bus.getName(), busStopName);
                spaceAvailable.await();
            }
            currentBuses.add(bus);
            log.info("{} arrived to the bus stop {}", bus.getName(), busStopName);
        } finally {
            lock.unlock();
        }
    }

    public void depart(Bus bus) {
        lock.lock();
        try {
            if (currentBuses.remove(bus)) {
                log.info("{} left the bus stop {}", bus.getName(), busStopName);
                spaceAvailable.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getBusCount() {
        lock.lock();
        try {
            return currentBuses.size();
        } finally {
            lock.unlock();
        }
    }
}
