package io.github.vm.patlego.iot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigLog;
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
        this.config = config;
        this.configLog = configLog;
    }

    public abstract String getModule();


    public MThreadState getState() {
        return this.state;
    }

    public void updateConfig(Config config) {
        if (config != null) {
            this.config = config;
        }
    }

    public Boolean keepRunning() {
        return this.config.isEnabled();
    }
}
