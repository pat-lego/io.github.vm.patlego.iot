package io.github.vm.patlego.iot.relay;

import io.github.vm.patlego.iot.config.Config;

public interface Relay {

    /**
     * Implementation is used to send data to an external system
     * @param config
     * @return
     * @throws RelayException
     */
    public Object execute(Config config) throws RelayException;
    
}
