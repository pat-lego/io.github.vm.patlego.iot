package io.github.vm.patlego.iot;

import io.github.vm.patlego.iot.config.ConfigRelay;

public class MainRelayType implements ConfigRelay {

    private String type;
    private String clazz;

    @Override
    public String getType() {
        switch (type.toUpperCase()) {
        case MainConfigHttp.GET:
            return MainConfigHttp.GET;
        case MainConfigHttp.POST:
            return MainConfigHttp.POST;
        case MainConfigHttp.PUT:
            return MainConfigHttp.PUT;
        case MainConfigHttp.DELETE:
            return MainConfigHttp.DELETE;
        default:
            throw new IllegalArgumentException("Cannot locate the necessary relay type from the specific config file");
        }
    }

    @Override
    public String getClassPath() {
       return this.clazz;
    }

}
