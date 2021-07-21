package io.github.vm.patlego.iot.server.commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;

@Service
@Command(scope = "iot", name = "read-sensor-config", description = "Update data within the Sensor Confg")
public class ReadSensorConfig implements Action {

    @Argument(index = 0, name = "configId", description = "The Sensor Config ID to be queried", required = true, multiValued = false)
    public int id = 0;

    @Reference
    private SensorConfigDS sensorConfigDS;

    @Override
    public Object execute() throws Exception {
       SensorConfig sensorConfig = this.sensorConfigDS.getConfig(id);
       return sensorConfig.toString();
    }


    
}
