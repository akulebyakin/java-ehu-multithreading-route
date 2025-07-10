package com.kulebiakin.route.reader;

import com.kulebiakin.route.model.Bus;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteLoaderTest {

    @Test
    void loadBusesReturnsEmptyListWhenFileIsEmpty() throws Exception {
        Path path = Files.createTempFile("empty-routes", ".txt");
        RouteLoader loader = new RouteLoader();
        List<Bus> buses = loader.loadBuses(path);
        assertThat(buses).isEmpty();
    }

    @Test
    void loadBusesSkipsCommentAndBlankLines() throws Exception {
        Path path = Files.createTempFile("routes", ".txt");
        Files.writeString(path, "\n# This is a comment\n\n");
        RouteLoader loader = new RouteLoader();
        List<Bus> buses = loader.loadBuses(path);
        assertThat(buses).isEmpty();
    }

    @Test
    void loadBusesHandlesInvalidWaitTimeGracefully() throws Exception {
        Path path = Files.createTempFile("routes", ".txt");
        Files.writeString(path, "Bus1;notANumber;StopA,StopB\n");
        RouteLoader loader = new RouteLoader();
        List<Bus> buses = loader.loadBuses(path);
        assertThat(buses).isEmpty();
    }

    @Test
    void loadBusesCreatesBusWithMultipleStops() throws Exception {
        Path path = Files.createTempFile("routes", ".txt");
        Files.writeString(path, "BusX;10;Stop1,Stop2,Stop3\n");
        RouteLoader loader = new RouteLoader();
        List<Bus> buses = loader.loadBuses(path);
        assertThat(buses).hasSize(1);
        assertThat(buses.get(0).getRoute()).hasSize(3);
        assertThat(buses.get(0).getName()).isEqualTo("BusX");
        assertThat(buses.get(0).getWaitSecondsPerStop()).isEqualTo(10);
    }

    @Test
    void loadBusesCreatesBusWithSingleStop() throws Exception {
        Path path = Files.createTempFile("routes", ".txt");
        Files.writeString(path, "SoloBus;5;OnlyStop\n");
        RouteLoader loader = new RouteLoader();
        List<Bus> buses = loader.loadBuses(path);
        assertThat(buses).hasSize(1);
        assertThat(buses.get(0).getRoute()).hasSize(1);
        assertThat(buses.get(0).getRoute().get(0).getBusStopName()).isEqualTo("OnlyStop");
    }
}
