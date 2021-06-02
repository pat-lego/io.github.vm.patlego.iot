package io.github.vm.patlego.iot.server.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

public class TestSensorUtils {

    @Test
    public void testCapitalize() {
        assertEquals("Pat", SensorUtils.capitalize("paT"));
        assertEquals(null, SensorUtils.capitalize(null));
        assertEquals(" ", SensorUtils.capitalize(" "));
    }

    @Test
    public void testTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/New_York"));
        Timestamp ts = Timestamp.valueOf(now);

        assertNotNull(SensorUtils.time(ts));
        System.out.println(SensorUtils.time(ts));
    }
    
}
