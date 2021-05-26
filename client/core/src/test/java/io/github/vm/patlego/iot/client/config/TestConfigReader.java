package io.github.vm.patlego.iot.client.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.github.vm.patlego.iot.client.MainConfigFile;

public class TestConfigReader {

    @Test
    public void testReader() throws IOException {
        ConfigFile main = ConfigReader.getConfigFile("src/test/resources/config.json", MainConfigFile.class);
        assertNotNull(main.getConfigs());
        assertEquals(2, main.getConfigs().size());

        assertEquals(main.getConfigs().stream().findFirst().get().getSystem().getURL().toString(), "http://localhost:8181/talk/to/me");
    }

    @Test
    public void testReader_withDuplicates() throws IOException {
        ConfigFile main = ConfigReader.getConfigFile("src/test/resources/duplicateconfig.json", MainConfigFile.class);
        assertNotNull(main.getConfigs());
        assertEquals(1, main.getConfigs().size());

        assertEquals(main.getConfigs().stream().findFirst().get().getSystem().getURL().toString(), "http://localhost:8181/talk/to/me");
    }
    
}
