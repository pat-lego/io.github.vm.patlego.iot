package io.github.vm.patlego.iot.client.config;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.Logger;
import io.github.vm.patlego.iot.client.MainConfigFile;

public class TestMainConfigLog {

    @Test
    public void testMainLogConfig() throws IOException {
        ConfigFile config = ConfigReader.getConfigFile("src/test/resources/config-log.json", MainConfigFile.class);
        Logger logger = config.getLogConfig().getLogger();
        logger.info("This is my message");
    }

    @Test
    public void testMainLogConfig_noPath() throws IOException {
        ConfigFile config = ConfigReader.getConfigFile("src/test/resources/config.json", MainConfigFile.class);
        Logger logger = config.getLogConfig().getLogger();
        logger.info("This is my message");
    }

    @AfterAll
    public static void finish() {
        File file = new File("src/test/resources/my-log-file.log");
        file.delete();
    }
    
}
