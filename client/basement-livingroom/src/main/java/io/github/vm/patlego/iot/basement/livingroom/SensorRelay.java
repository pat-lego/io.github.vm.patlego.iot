package io.github.vm.patlego.iot.basement.livingroom;

import java.io.IOException;

import com.google.gson.JsonObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.github.vm.patlego.iot.client.Types;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.relay.HttpMsg;
import io.github.vm.patlego.iot.client.relay.HttpRelay;
import io.github.vm.patlego.iot.client.relay.RelayException;
import io.github.vm.patlego.iot.security.Security;
import io.github.vm.patlego.iot.security.SimpleSecurity;

public class SensorRelay extends HttpRelay {

    private Security security;

    private final String THREAD = "thread";
    private final String TYPE = "type";
    private final String LOCATION = "location";
    private final String SENSOR_CONFIG_ID = "sensorConfigId";

    public SensorRelay() {
        this.security = new SimpleSecurity();
    }

    @Override
    public String getToken(Config config) {
        if (Boolean.TRUE.equals(config.getSystem().hasAuth())) {
            if (Boolean.TRUE.equals(config.getSystem().getAuth().isEncrypted())) {
                return this.security.decrypt(config.getSystem().getAuth().getAuthorization());
            }
            return config.getSystem().getAuth().getAuthorization();
        }

        return null;
    }

    @Override
    public CloseableHttpResponse get(Config config, HttpMsg msg) throws RelayException {
        throw new UnsupportedOperationException("GET request not supported for sensor relay");
    }

    @Override
    public CloseableHttpResponse put(Config config, HttpMsg msg) throws RelayException {
        throw new UnsupportedOperationException("PUT request not supported for sensor relay");
    }

    @Override
    public CloseableHttpResponse post(Config config, HttpMsg msg) throws RelayException {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(config.getSystem().getURL().toString());
            JsonObject body = new JsonObject();
            body.addProperty(TYPE, Types.PIR.toString());
            body.addProperty(LOCATION, "BASEMENT");
            body.addProperty(THREAD, config.getThread());
            body.addProperty(SENSOR_CONFIG_ID, 1);

            post.setEntity(new StringEntity(body.toString()));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            if (null != getToken(config)) {
                post.setHeader(getAuthHeader(), getToken(config));
            }

            return client.execute(post);

        } catch (IOException e) {
            throw new RelayException(e);
        }
    }

    @Override
    public CloseableHttpResponse delete(Config config, HttpMsg msg) throws RelayException {
        throw new UnsupportedOperationException("POST request not supported for sensor relay");
    }

}
