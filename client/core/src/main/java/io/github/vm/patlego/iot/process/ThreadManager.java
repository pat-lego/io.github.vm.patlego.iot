package io.github.vm.patlego.iot.process;

import java.util.HashMap;
import java.util.Map;

import io.github.vm.patlego.iot.config.ConfigFile;

public class ThreadManager {

    private Boolean IS_RUNNING = Boolean.FALSE;
    private Map<String, Thread> threads = new HashMap();
    
    public void start(ConfigFile configFile) {
        
    }
    
}
