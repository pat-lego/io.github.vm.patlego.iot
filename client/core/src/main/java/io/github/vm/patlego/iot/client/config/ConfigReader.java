package io.github.vm.patlego.iot.client.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;


public class ConfigReader {

    public static ConfigFile getConfigFile(String path, Class<? extends ConfigFile> type) throws IOException {
    
        try (InputStream in = new FileInputStream(path)) {
            String config = IOUtils.toString(in, StandardCharsets.UTF_8);
            return new Gson().fromJson(config, type);
        }
    }

}
