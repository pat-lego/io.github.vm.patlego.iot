package io.github.vm.patlego.iot;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.System;

public class MainConfig implements Config {

    private String module;
    private String thread;
    private MainSystem system;

    @Override
    public String getModule() {
       return this.module;
    }

    @Override
    public String getThread() {
       return this.thread;
    }

    @Override
    public System getSystem() {
        return this.system;
    }
    
}
