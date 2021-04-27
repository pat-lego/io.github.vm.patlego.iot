package io.github.vm.patlego.iot.config;

import java.net.MalformedURLException;
import java.net.URL;

public interface System {
    
    public URL getURL() throws MalformedURLException;

    public Boolean hasAuth();

    public Auth getAuth();
}
