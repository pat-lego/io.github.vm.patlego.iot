package io.github.vm.patlego.iot.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.github.vm.patlego.iot.MainConfigFile;

public class TestConfigReader {

    @Test
    public void testReader() throws IOException {
        ConfigFile main = ConfigReader.getConfigFile("src/test/resources/config.json", MainConfigFile.class);
        assertNotNull(main.getConfig("sensor1"));
        assertEquals(2, main.getConfigs().size());

        assertEquals(main.getConfig("sensor1").getSystem().getURL().toString(), "http://localhost:8181/talk/to/me");
    }
    
}
