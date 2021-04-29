package io.github.vm.patlego.iot;

import java.util.Optional;

import io.github.vm.patlego.iot.config.Auth;
import io.github.vm.patlego.iot.config.AuthType;

public class MainAuth implements Auth {

    private String authType;
    private String token;
    private Boolean isEncrypted;

    @Override
    public AuthType getType() {
        return AuthType.valueOf(this.authType);
    }

    @Override
    public String getAuthorization() {
       return this.token;
    }

    @Override
    public Boolean isEncrypted() {
       return Optional.ofNullable(this.isEncrypted).orElse(Boolean.FALSE);
    }
    
}
