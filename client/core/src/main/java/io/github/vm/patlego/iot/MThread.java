package io.github.vm.patlego.iot;

public interface MThread extends Runnable {
    
    public Boolean isRunning();

    public void stop();

    public String getModule();
}
