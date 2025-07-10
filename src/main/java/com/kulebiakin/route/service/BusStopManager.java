package com.kulebiakin.route.service;

import com.kulebiakin.route.model.BusStop;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BusStopManager {
    private final ConcurrentMap<String, BusStop> stops = new ConcurrentHashMap<>();

    private BusStopManager() {
    }

    public static BusStopManager getInstance() {
        return Holder.INSTANCE;
    }

    public BusStop getOrCreateStop(String name) {
        return stops.computeIfAbsent(name, n -> new BusStop(n, 2)); // default 2 buses per stop
    }

    public Collection<BusStop> getAllStops() {
        return stops.values();
    }

    private static class Holder {
        private static final BusStopManager INSTANCE = new BusStopManager();
    }
}
