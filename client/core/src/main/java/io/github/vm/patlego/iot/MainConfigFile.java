package io.github.vm.patlego.iot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigFile;

public class MainConfigFile implements ConfigFile {

    private HashSet<MainConfig> configs;
    private Boolean halt;
    
    @Override
    public Set<MainConfig> getConfigs() {
        return this.configs;
    }

    @Override
    public Config getConfig(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Module name must be defined when calling this function - currently it is null or blank");
        }

        for (Config config: configs) {
            if (name.equals(config.getModule())) {
                return config;
            }
        }

        return null;
    }

    @Override
    public Boolean haltSystem() {
        return this.halt;
    }
    
}
