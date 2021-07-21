package io.github.vm.patlego.iot.client;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.vm.patlego.iot.client.config.Auth;
import io.github.vm.patlego.iot.client.config.ConfigRelay;
import io.github.vm.patlego.iot.client.config.ConfigSystem;

public class MainConfigSystem implements ConfigSystem {

    private String url;
    private MainAuth auth;
    private MainConfigRelay relay;

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(this.url);
    }

    @Override
    public Boolean hasAuth() {
        return this.auth != null;
    }

    @Override
    public Auth getAuth() {
        return this.auth;
    }

    @Override
    public ConfigRelay getRelay() {
        return this.relay;
    }
    
}
