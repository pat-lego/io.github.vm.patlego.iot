package io.github.vm.patlego.iot.server.sensor;

import java.util.List;

import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;

public interface SensorService {

    public SensorEvent createSensorEvent(SensorEvent event);

    public SensorEvent getSensorEvent(long id);

    public List<SensorEvent> getSensorEvents();
}