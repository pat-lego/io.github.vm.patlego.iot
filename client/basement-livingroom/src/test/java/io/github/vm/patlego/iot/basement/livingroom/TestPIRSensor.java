package io.github.vm.patlego.iot.basement.livingroom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.vm.patlego.iot.client.config.ConfigReader;

public class TestPIRSensor {

    @Test
    public void testPiRSensorName() {
        ConfigReader configReader = Mockito.mock(ConfigReader.class);
        PIRSensor pirSensor = new PIRSensor(configReader);

        assertEquals("io.github.vm.patlego.iot.basement.livingroom.PIRSensor", pirSensor.getClassPath());
    }
    
}
