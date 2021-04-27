package io.github.vm.patlego.iot.config;

import java.net.URL;

public interface System {
    
    public URL getURL();

    public Boolean hasAuth();

    public Auth getAuth();
}
