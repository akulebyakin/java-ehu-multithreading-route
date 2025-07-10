package com.kulebiakin.route.service;

import com.kulebiakin.route.model.BusStop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BusStopManager {
    private static final BusStopManager instance = new BusStopManager();
    private final ConcurrentMap<String, BusStop> stops = new ConcurrentHashMap<>();

    private BusStopManager() {
    }

    public static BusStopManager getInstance() {
        return instance;
    }

    public BusStop getOrCreateStop(String name) {
        return stops.computeIfAbsent(name, n -> new BusStop(n, 2));
    }

    public Iterable<BusStop> getAllStops() {
        return stops.values();
    }
}
