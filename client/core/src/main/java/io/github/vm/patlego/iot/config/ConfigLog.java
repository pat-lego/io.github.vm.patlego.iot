package io.github.vm.patlego.iot.config;

import ch.qos.logback.classic.Logger;

public interface ConfigLog {

    public String getName();
    
    public String getPath();
    
    public Logger getLogger();
}
