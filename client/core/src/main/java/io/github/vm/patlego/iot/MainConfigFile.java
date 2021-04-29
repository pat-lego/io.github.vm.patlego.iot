package io.github.vm.patlego.iot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigFile;
import io.github.vm.patlego.iot.config.ConfigLog;

public class MainConfigFile implements ConfigFile {

    private HashSet<MainConfig> configs;
    private Boolean halt;
    private Long timeout;
    private MainConfigLog logConfig;
    
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
        return Optional.ofNullable(this.halt).orElse(Boolean.TRUE);
    }

    @Override
    public Long getTimeout() {
        return Optional.ofNullable(this.timeout).orElse(0L);
    }

    @Override
    public ConfigLog getLogConfig() {
        if (this.logConfig == null) {
            this.logConfig = new MainConfigLog();
        }
        return this.logConfig;
    }
    
}
