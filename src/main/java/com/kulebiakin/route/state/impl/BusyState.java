package com.kulebiakin.route.state.impl;

import com.kulebiakin.route.model.BusStop;
import com.kulebiakin.route.state.StopState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusyState implements StopState {
    private static final Logger log = LoggerFactory.getLogger(BusyState.class);

    @Override
    public void handle(BusStop stop) {
        log.info("Stop {} is occupied", stop.getBusStopName());
    }

    @Override
    public String getStateName() {
        return "BUSY";
    }
}
