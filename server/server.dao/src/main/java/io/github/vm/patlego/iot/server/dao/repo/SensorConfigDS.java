package io.github.vm.patlego.iot.server.dao.repo;

import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;

public interface SensorConfigDS {
    
    public SensorConfig getConfig(long id);

    public SensorConfig getConfigByThreadName(String name);

    public SensorConfig updateConfig(SensorConfig sensorConfig);
}
