package io.github.vm.patlego.iot.client.config;

public interface Config {
    
    /**
     * Return the name of the module which is defined within the thread
     * @return String
     */
    public String getModule();

    /**
     * Returns the class path to the thread that will perform the interaction with the system
     * @return String
     */
    public String getThread();

    /**
     * Returns the system that the application will talk to when retrieving data
     * @return System
     */
    public ConfigSystem getSystem();

    /**
     * Determines if this module is enabled for the given system
     * @return
     */
    public boolean isEnabled();

    public boolean equals(Object a);

    public int hashCode();

}
