package io.github.vm.patlego.iot.server.dao.tables;

import io.github.vm.patlego.iot.server.dao.tables.config.Config;
import io.github.vm.patlego.iot.server.dao.tables.config.ConfigSystem;


public class MainConfig implements Config {

    private String module;
    private String thread;
    private Boolean logEntry;
    private Boolean enableSms;
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
    public ConfigSystem getSystem() {
        return this.system;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        for (char c : this.getModule().toCharArray()) {
            hash = hash * 31 + c;
        }

        return hash;
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof SensorConfig && this.hashCode() == a.hashCode()) {
            return true;
        }

        return false;
    }

    @Override
    public Boolean enableDBLogging() {
        return this.logEntry;
    }

    @Override
    public Boolean enableSms() {
       return this.enableSms;
    }

}
