package io.github.vm.patlego.iot.server.dao.tables.config;

public interface Auth {

    public String getAuthorization();

    public void setAuthorization(String authorization);

    public Boolean isEncrypted();

    public void setIsEncrypted(Boolean isEncrypted);
    
}
