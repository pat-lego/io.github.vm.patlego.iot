package io.github.vm.patlego.iot.config;

public interface Auth {

    public AuthType getType();

    public String getAuthorization();

    public Boolean isEncrypted();
    
}
