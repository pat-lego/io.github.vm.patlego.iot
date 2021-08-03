package io.github.vm.patlego.iot.server.dao;

import org.junit.jupiter.api.Test;

import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;

public class TestSensorConfig {
    
    @Test
    public void testPrettyPrint() {
        SensorConfig config = new SensorConfig();
        config.setConfigId(1L);
        config.setConfig("{\"enable\": false, \"module\": \"Basement Living Room\", \"system\": {\"url\": \"https://www.pat-lego.com/cxf/sensors/events\", \"auth\": {\"token\": \"\", \"isEncrypted\": true}, \"hasAuth\": true, \"configRelay\": {\"type\": \"POST\", \"clazz\": \"io.github.vm.patlego.iot.basement.livingroom.SensorRelay\"}}, \"thread\": \"io.github.vm.patlego.iot.basement.livingroom.PIRSensor\"}");
        config.setKey("1234567");

        System.out.println(config.toString());
    }
}
