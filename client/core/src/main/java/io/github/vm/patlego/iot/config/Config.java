package io.github.vm.patlego.iot.config;

public interface Config {
    
    /**
     * Return the name of the module which is defined within the thread
     * @return
     */
    public String getModule();

    /**
     * Returns the class path to the thread that will perform the interaction with the system
     * @return
     */
    public String getThread();

    public System getSystem();

}
