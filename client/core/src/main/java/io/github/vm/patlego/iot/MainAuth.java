package io.github.vm.patlego.iot;

import java.util.Optional;

import io.github.vm.patlego.iot.config.Auth;

public class MainAuth implements Auth {

    private String token;
    private Boolean isEncrypted;

    @Override
    public String getAuthorization() {
       return this.token;
    }

    @Override
    public Boolean isEncrypted() {
       return Optional.ofNullable(this.isEncrypted).orElse(Boolean.FALSE);
    }
    
}
