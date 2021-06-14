package io.github.vm.patlego.iot.client.relay;

import io.github.vm.patlego.iot.client.config.Config;

public interface Relay {

    /**
     * Implementation is used to send data to an external system
     * @param message
     * @param config
     * @return
     * @throws RelayException
     */
    public Object execute(Config config, HttpMsg data) throws RelayException;
    
}
