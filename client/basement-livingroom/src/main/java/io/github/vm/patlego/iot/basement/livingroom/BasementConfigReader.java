package io.github.vm.patlego.iot.basement.livingroom;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.client.MainConfig;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.config.ConfigReader;

public class BasementConfigReader implements ConfigReader {

    private final String AUTHORIZATION = "Authorization";
    private String url = "https://www.pat-lego.com/cxf/sensors/configs/1";

    @Override
    public List<Config> getConfigs() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader(AUTHORIZATION, getToken());
            
            ResponseHandler<List<Config>> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    String entity = EntityUtils.toString(responseEntity);
                    Config config = new GsonBuilder().create().fromJson(entity, MainConfig.class);
                    return Arrays.asList(config);
                }
                throw new ClientProtocolException(String.format(
                        "Error in retrieving the Config for ID 1, received the following HTTP Code %d",
                        status));

            };
            return client.execute(get, responseHandler);
        }

    }

    private String getToken() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getResourceAsStream("/basement/livingroom/basement-livingroom.properties"));
        return props.getProperty("basement-livingroom");
    }

}
