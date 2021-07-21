package io.github.vm.patlego.iot.server.commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;
import io.github.vm.patlego.iot.server.dao.tables.config.Config;

@Service
@Command(scope = "iot", name = "update-sensor-config", description = "Update data within the Sensor Confg")
public class UpdateSensorConfig implements Action {

    @Argument(index = 0, name = "configId", description = "The Sensor Config ID to update", required = true, multiValued = false)
    public int id = 0;

    @Argument(index = 1, name = "enabled", description = "Disable or enable the sensor", required = true, multiValued = false)
    public Boolean enabled = null;

    @Reference
    private SensorConfigDS sensorConfigDS;

    @Override
    public Object execute() throws Exception {
       SensorConfig sensorConfig = this.sensorConfigDS.getConfig(id);
       Config config = SensorConfig.toConfig(sensorConfig.getConfig());
       
       config.setIsEnabled(enabled);
       sensorConfig.setConfig(SensorConfig.toString(config));
       this.sensorConfigDS.updateConfig(sensorConfig);

       return String.format("The sensor config with id %d has been successfully updated", id);
    }


    
}
