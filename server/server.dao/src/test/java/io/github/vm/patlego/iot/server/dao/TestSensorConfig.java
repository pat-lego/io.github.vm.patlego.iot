package io.github.vm.patlego.iot.server.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;

public class TestSensorConfig {
    
    @Test
    public void testPrettyPrint() {
        String prettyPrint = SensorConfig.prettyPrintConfig("{\"enable\": false, \"module\": \"Basement Living Room\", \"system\": {\"url\": \"https://www.pat-lego.com/cxf/sensors/events\", \"auth\": {\"token\": \"\", \"isEncrypted\": true}, \"hasAuth\": true, \"configRelay\": {\"type\": \"POST\", \"clazz\": \"io.github.vm.patlego.iot.basement.livingroom.SensorRelay\"}}, \"thread\": \"io.github.vm.patlego.iot.basement.livingroom.PIRSensor\"}");
        assertTrue(prettyPrint.contains("\n"));
    }
}
