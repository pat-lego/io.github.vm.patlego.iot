package io.github.vm.patlego.iot;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.vm.patlego.iot.config.Auth;
import io.github.vm.patlego.iot.config.System;

public class MainSystem implements System {

    private String url;
    private Boolean hasAuth;
    private MainAuth auth;

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
    
}
