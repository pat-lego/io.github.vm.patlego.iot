package io.github.vm.patlego.iot.server;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import io.github.vm.patlego.iot.server.sensor.interceptor.AuthenticationInterceptor;
import io.github.vm.patlego.iot.server.sensor.servlets.SensorServiceServlet;

import org.apache.cxf.interceptor.Interceptor;

@Component
public class SensorRestService {
    
    public Server server;

    @Activate
    public void activate() throws Exception {
        JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();
        bean.setAddress(SensorServicePath.SENSOR_PATH);
        bean.setBus(BusFactory.getDefaultBus());
        bean.setProvider(new JacksonJsonProvider());
        bean.setServiceBean(new SensorServiceServlet());
            
        server = bean.create();
        server.getEndpoint().getInInterceptors().add(new AuthenticationInterceptor());
    
    }

    @Modified
    public void modified() throws Exception {
        deactivate();
        activate();
    }

    @Deactivate
    public void deactivate() throws Exception {
        if (server != null) {
            server.destroy();
        }
    }
}