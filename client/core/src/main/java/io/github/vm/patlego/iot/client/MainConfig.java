package io.github.vm.patlego.iot.client;

import com.google.gson.GsonBuilder;

import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.config.ConfigSystem;

public class MainConfig implements Config {

    private String module;
    private String thread;
    private MainConfigSystem system;

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
        if (a instanceof Config && this.hashCode() == a.hashCode()) {
            return true;
        }

        return false;
    }

    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
