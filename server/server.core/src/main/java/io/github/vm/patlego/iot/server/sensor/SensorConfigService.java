package io.github.vm.patlego.iot.server.sensor;

import io.github.vm.patlego.iot.server.dao.tables.config.Config;

public interface SensorConfigService {
    
    public Config getConfig(long id);
}
