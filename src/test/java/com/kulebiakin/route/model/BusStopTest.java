package com.kulebiakin.route.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BusStopTest {

    @Test
    void busCannotArriveWhenStopIsFullAndWaitsUntilSpaceIsAvailable() throws InterruptedException {
        BusStop stop = new BusStop("FullStop", 1);
        Bus firstBus = new Bus("FirstBus", List.of(stop), 0);
        Bus secondBus = new Bus("SecondBus", List.of(stop), 0);

        stop.arrive(firstBus);

        Thread secondBusThread = new Thread(() -> {
            try {
                stop.arrive(secondBus);
            } catch (InterruptedException ignored) {
            }
        });
        secondBusThread.start();

        TimeUnit.MILLISECONDS.sleep(100);
        assertThat(stop.getBusCount()).isEqualTo(1);

        stop.depart(firstBus);
        secondBusThread.join(500);

        assertThat(stop.getBusCount()).isEqualTo(1);
        assertThat(stop.getBusCount()).isNotZero();
    }

    @Test
    void departDoesNothingIfBusIsNotPresent() {
        BusStop stop = new BusStop("Nowhere", 2);
        Bus bus = new Bus("GhostBus", List.of(stop), 0);

        stop.depart(bus);

        assertThat(stop.getBusCount()).isZero();
    }

    @Test
    void getBusStopNameReturnsCorrectName() {
        BusStop stop = new BusStop("Central", 3);
        assertThat(stop.getBusStopName()).isEqualTo("Central");
    }
}
