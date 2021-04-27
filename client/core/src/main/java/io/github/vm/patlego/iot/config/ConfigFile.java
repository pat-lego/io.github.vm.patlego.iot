package io.github.vm.patlego.iot.config;

import java.util.Set;

public interface ConfigFile {
    
    public Set<? extends Config> getConfigs();
    
    public Boolean haltSystem();

    public Config getConfig(String name);
}
