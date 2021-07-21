package io.github.vm.patlego.iot.basement.livingroom;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.config.ConfigReader;

public class BasementConfigReader implements ConfigReader {

    private CloseableHttpClient client = HttpClients.createDefault();

    @Override
    public List<Config> getConfigs() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
