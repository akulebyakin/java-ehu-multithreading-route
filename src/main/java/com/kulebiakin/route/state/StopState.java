package com.kulebiakin.route.state;

import com.kulebiakin.route.model.BusStop;

public interface StopState {
    void handle(BusStop stop);

    String getStateName();
}
