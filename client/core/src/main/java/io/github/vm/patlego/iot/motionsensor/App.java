package io.github.vm.patlego.iot.motionsensor;

import io.github.vm.patlego.iot.MThread;

public class App implements MThread {

    private Boolean IS_RUNNING = Boolean.FALSE;

    @Override
    public void run() {
        
    }
    

    @Override
    public Boolean isRunning() {
       return this.IS_RUNNING;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }
    
}
