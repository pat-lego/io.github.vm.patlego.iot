package io.github.vm.patlego.iot.client;

import java.util.Optional;

import io.github.vm.patlego.iot.client.config.Auth;

public class MainAuth implements Auth {

    private String authorization;
    private Boolean encrypted;

    @Override
    public String getAuthorization() {
       return this.authorization;
    }

    @Override
    public Boolean isEncrypted() {
       return Optional.ofNullable(this.encrypted).orElse(Boolean.FALSE);
    }
    
}
