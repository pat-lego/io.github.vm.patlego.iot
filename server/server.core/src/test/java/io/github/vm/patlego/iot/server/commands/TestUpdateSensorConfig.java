package io.github.vm.patlego.iot.server.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;

public class TestUpdateSensorConfig {

    @Test
    public void testUpdateSensorConfig() throws Exception {
        UpdateSensorConfig updateSensor = new UpdateSensorConfig();
        SensorConfig sensorConfig = new SensorConfig();
        sensorConfig.setConfig("{ \"module\": \"Basement Living Room\", \"thread\": \"io.github.vm.patlego.iot.basement.livingroom.PIRSensor\", \"logEntry\": true, \"enableSms\": true, \"system\": { \"url\": \"https://www.pat-lego.com/cxf/sensors/events\", \"hasAuth\": true, \"auth\": { \"token\": \"\", \"isEncrypted\": true }, \"configRelay\": { \"type\": \"POST\", \"clazz\": \"io.github.vm.patlego.iot.basement.livingroom.SensorRelay\" } } }");
        
        SensorConfigDS configDS = Mockito.mock(SensorConfigDS.class);
        Mockito.when(configDS.getConfig(1L)).thenReturn(sensorConfig);

        updateSensor.sensorConfigDS = configDS;
        updateSensor.id = 1L;
        updateSensor.jsonPath = "..logEntry";
        updateSensor.value = "Bool:False";

        updateSensor.execute();
    }
    
}
