package io.github.vm.patlego.iot.client;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.vm.patlego.iot.client.config.Auth;
import io.github.vm.patlego.iot.client.config.ConfigRelay;
import io.github.vm.patlego.iot.client.config.ConfigSystem;

public class MainSystem implements ConfigSystem {

    private String url;
    private Boolean hasAuth;
    private MainAuth auth;
    private MainConfigRelay configRelay;

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(this.url);
    }

    @Override
    public Boolean hasAuth() {
        return this.hasAuth;
    }

    @Override
    public Auth getAuth() {
        return this.auth;
    }

    @Override
    public ConfigRelay getRelay() {
        return this.configRelay;
    }
    
}
