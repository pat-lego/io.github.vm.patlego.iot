package io.github.vm.patlego.iot.threads;

import io.github.vm.patlego.iot.MThread;
import io.github.vm.patlego.iot.MainConfigLog;
import io.github.vm.patlego.iot.config.Config;
public class App2 extends MThread {

    public App2(Config config) {
        super(config, new MainConfigLog());
    }

    @Override
    public void run() {
        this.state = MThreadState.RUNNING;
        System.out.println("App2 is running");
        this.state = MThreadState.STOPPED;
    }

    @Override
    public String getModule() {
        return "sensor2";
    }
    
}
