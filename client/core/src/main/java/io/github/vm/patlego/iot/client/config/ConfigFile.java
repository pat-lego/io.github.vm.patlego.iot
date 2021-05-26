package io.github.vm.patlego.iot.client.config;

import java.util.Set;

public interface ConfigFile {
    
    public Set<? extends Config> getConfigs();
    
    public Boolean haltSystem();
    
    public Long getTimeout();

    public Config getConfig(String name);

    public ConfigLog getLogConfig();
}
