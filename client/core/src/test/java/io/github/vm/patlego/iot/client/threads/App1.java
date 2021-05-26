package io.github.vm.patlego.iot.client.threads;

import io.github.vm.patlego.iot.client.MThread;
import io.github.vm.patlego.iot.client.MainConfigLog;
import io.github.vm.patlego.iot.client.config.Config;

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
