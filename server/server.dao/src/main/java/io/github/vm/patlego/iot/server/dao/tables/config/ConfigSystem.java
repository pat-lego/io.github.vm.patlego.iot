package io.github.vm.patlego.iot.server.dao.tables.config;

import java.net.MalformedURLException;
import java.net.URL;

public interface ConfigSystem {
    
    public URL getURL() throws MalformedURLException;

    public Boolean hasAuth();

    public Auth getAuth();

    public ConfigRelay getRelay();
}
