package io.github.vm.patlego.iot.threads;

import io.github.vm.patlego.iot.MThread;
import io.github.vm.patlego.iot.MainConfigLog;
import io.github.vm.patlego.iot.config.Config;

public class App1 extends MThread {

    public App1(Config config) {
        super(config, new MainConfigLog());
    }

    @Override
    public void run() {
        this.state = MThreadState.RUNNING;
        System.out.println("App1 is running");
        this.state = MThreadState.STOPPED;

    }

    @Override
    public String getModule() {
        return "sensor1";
    }

}
