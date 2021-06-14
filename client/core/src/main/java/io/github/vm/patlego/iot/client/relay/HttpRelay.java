package io.github.vm.patlego.iot.client.relay;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.github.vm.patlego.iot.client.MainConfigHttp;
import io.github.vm.patlego.iot.client.config.Config;

public abstract class HttpRelay implements Relay {

    private final String AUTHORIZATION = "Authorization";
    protected CloseableHttpClient client = HttpClients.createDefault();

    public final String getAuthHeader() {
        return AUTHORIZATION;
    }

    public abstract String getToken(Config config);

    public abstract CloseableHttpResponse get(Config config, HttpMsg data) throws RelayException;
    public abstract CloseableHttpResponse put(Config config, HttpMsg data) throws RelayException;
    public abstract CloseableHttpResponse post(Config config, HttpMsg data) throws RelayException;
    public abstract CloseableHttpResponse delete(Config config, HttpMsg data) throws RelayException;

    @Override
    public Object execute(Config config, HttpMsg data) throws RelayException {
        if (config.getSystem().getRelay().getType().equals(MainConfigHttp.GET)) {
            return this.get(config, data);
        }

        if (config.getSystem().getRelay().getType().equals(MainConfigHttp.POST)) {
            return this.post(config, data);
        }

        if (config.getSystem().getRelay().getType().equals(MainConfigHttp.PUT)) {
            return this.put(config, data);
        }

        if (config.getSystem().getRelay().getType().equals(MainConfigHttp.DELETE)) {
            return this.delete(config, data);
        }

        throw new RelayException(String.format("No matching relay type located was given %s as relay type", config.getSystem().getRelay().getType()));
    }
    
}
