package io.github.vm.patlego.iot;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.System;

public class MainConfig implements Config {

    private String module;
    private String thread;
    private Boolean enable;
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

    @Override
    public Boolean isEnabled() {
        return this.enable;
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
        if (a instanceof Config && this.hashCode() == a.hashCode()) {
            return true;
        }

        return false;
    }

}
