package io.github.vm.patlego.iot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.process.MThreadState;

public abstract class MThread implements Runnable {

    private MThreadState state = MThreadState.INITIALIZED;
    
    protected int sleepTime = 50;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Config config;
    protected GpioPin pin;
    protected final GpioController gpio = GpioFactory.getInstance();


    protected MThread(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Cannot provide a null config object to the MThread");
        }
        this.config = config;
    }

    public abstract String getModule();

    @Override
    public void run() {
        this.state = MThreadState.RUNNING;
        Boolean execute = this.keepRunning();

        try {
            this.execute();
        
            while (Boolean.TRUE.equals(execute)) {
                Thread.sleep(sleepTime);
            }
        } catch (MThreadExecutionException e) {
            this.state = MThreadState.FAILED;
            return;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            this.stop();
            this.state = MThreadState.STOPPED;
        } catch (MThreadStopException e) {
            this.state = MThreadState.FAILED;
        }

    }

    /**
     * Create the GioPin used to communicate with external systems, register any listeners here and make sure to set the GpioPin global variable in order to keep it running throughout
     * the lifetime of this thread
     * @throws MThreadExecutionException
     */
    public abstract void execute() throws MThreadExecutionException;

    public abstract void stop() throws MThreadStopException;

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
