package io.github.vm.patlego.iot.config;

import java.util.List;

public interface ConfigFile {
    
    public List<? extends Config> getConfigs();

    public Config getConfig(String name);
}
