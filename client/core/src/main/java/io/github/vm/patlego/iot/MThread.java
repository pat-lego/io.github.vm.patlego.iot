package io.github.vm.patlego.iot;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigLog;
import io.github.vm.patlego.iot.config.ConfigSystem;
import io.github.vm.patlego.iot.relay.Relay;
import io.github.vm.patlego.iot.threads.MThreadState;

public abstract class MThread implements Runnable {

    protected MThreadState state = MThreadState.INITIALIZED;
    protected int sleepTime = 50;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Config config;
    protected ConfigLog configLog;

    protected MThread(Config config, ConfigLog configLog) {
        if (config == null) {
            throw new IllegalArgumentException("Cannot provide a null config object to the MThread");
        }

        if (configLog == null) {
            throw new IllegalArgumentException("Cannot provide a null configLog object to the MThread");
        }
        this.config = config;
        this.configLog = configLog;
    }

    public abstract String getModule();

    public Relay getRelay(ConfigSystem configSystem) {
        try {
            Class<Relay> relayClass = (Class<Relay>) Class.forName(configSystem.getRelay().getClassPath());
            Constructor<Relay> relayClassConstructor = relayClass.getConstructor();
            return relayClassConstructor.newInstance();
        } catch (Exception e) {
            return null;
        }

    }

    public MThreadState getState() {
        return this.state;
    }

    public void updateConfig(Config config) {
        this.config = config;

    }

    public Boolean keepRunning() {
        return this.config.isEnabled();
    }
}
