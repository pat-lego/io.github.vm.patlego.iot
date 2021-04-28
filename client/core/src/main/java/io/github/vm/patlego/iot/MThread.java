package io.github.vm.patlego.iot;

import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.process.MThreadState;

public abstract class MThread implements Runnable {

    private Config config;
    private MThreadState state = MThreadState.INITIALIZED;
    protected int sleepTime = 1000;

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

        while (Boolean.TRUE.equals(execute)) {
            try {
                this.execute();
                Thread.sleep(sleepTime);
            } catch (MThreadExecutionException e) {
                this.state = MThreadState.FAILED;
                break;
            } catch (InterruptedException e) {
                this.state = MThreadState.FAILED;
                Thread.currentThread().interrupt();
                break;
            }
        }

        try {
            this.stop();
            this.state = MThreadState.STOPPED;
        } catch (MThreadStopException e) {
            this.state = MThreadState.FAILED;
        }
    }

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
