package io.github.vm.patlego.iot.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.config.ConfigReader;
import io.github.vm.patlego.iot.client.config.ConfigSystem;
import io.github.vm.patlego.iot.client.relay.Relay;
import io.github.vm.patlego.iot.client.relay.RelayInstantiationException;
import io.github.vm.patlego.iot.client.threads.MThreadState;

public abstract class MThread implements Runnable {

    protected MThreadState state = MThreadState.INITIALIZED;
    protected int sleepTime = 50;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ConfigReader configReader;
    
    protected MThread(ConfigReader configReader) {
        if (configReader == null) {
            throw new IllegalArgumentException("Cannot provide a null config object to the MThread");
        }

        this.configReader = configReader;
    }

    public abstract String getModule();

    public Relay getRelay(ConfigSystem configSystem) throws RelayInstantiationException {
        try {
            Class<Relay> relayClass = (Class<Relay>) Class.forName(configSystem.getRelay().getClassPath());
            Constructor<Relay> relayClassConstructor = relayClass.getConstructor();
            return relayClassConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RelayInstantiationException(e.getMessage(), e);
        }

    }

    public MThreadState getState() {
        return this.state;
    }

    public Config getConfig() throws Exception {
        return this.configReader.getConfigs().stream().filter(c -> c.getModule().equals(this.getModule())).findFirst().orElse(null);
    }
}
