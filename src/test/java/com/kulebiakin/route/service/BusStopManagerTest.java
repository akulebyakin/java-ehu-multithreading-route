package com.kulebiakin.route.service;

import com.kulebiakin.route.model.BusStop;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusStopManagerTest {

    @Test
    void testSingletonInstanceIsSame() {
        BusStopManager instance1 = BusStopManager.getInstance();
        BusStopManager instance2 = BusStopManager.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testCreateAndGetBusStop() {
        BusStopManager manager = BusStopManager.getInstance();
        BusStop stop1 = manager.getOrCreateStop("TestStop");

        assertThat(stop1).isNotNull();
        assertThat(stop1.getBusStopName()).isEqualTo("TestStop");

        BusStop stop2 = manager.getOrCreateStop("TestStop");
        assertThat(stop2).isSameAs(stop1);
    }
}
