package io.github.vm.patlego.iot.server.dao.repo;

import java.util.List;

import io.github.vm.patlego.iot.server.dao.exceptions.InvalidSensorEventException;
import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;

public interface SensorEventDS {
    
    public SensorEvent createEvent(SensorEvent event);

    public SensorEvent getEvent(long id) throws InvalidSensorEventException;

    public List<SensorEvent> getEvents();
}
