package io.github.vm.patlego.iot.server;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;
import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.repo.SensorEventDS;
import io.github.vm.patlego.iot.server.sensor.interceptor.SensorConfigAuthenticationInterceptor;
import io.github.vm.patlego.iot.server.sensor.interceptor.SensorEventAuthenticationInterceptor;
import io.github.vm.patlego.iot.server.sensor.interceptor.SensorEventInterceptor;
import io.github.vm.patlego.iot.server.sensor.servlets.SensorConfigsServiceServlet;
import io.github.vm.patlego.iot.server.sensor.servlets.SensorEventsServiceServlet;
import io.github.vm.patlego.sms.sender.SMSService;

@Component(immediate = true)
public class SensorRestService {

    @Reference
    private Authentication<Jwt> jwtAuthentication;

    @Reference
    private SMSService smsService;

    @Reference
    private SensorEventDS sensorEventDS;

    @Reference
    private Security securityService;

    @Reference
    private SensorConfigDS sensorConfigDS;
    
    private Server server;

    @Activate
    public void activate() throws Exception {
        JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();
        bean.setAddress(SensorServicePath.SENSOR_PATH);
        bean.setBus(BusFactory.getDefaultBus());
        bean.setProvider(new JacksonJsonProvider());
        bean.setServiceBean(new SensorEventsServiceServlet(sensorEventDS, smsService, securityService));
        bean.setServiceBean(new SensorConfigsServiceServlet(jwtAuthentication, sensorConfigDS, securityService));
            
        server = bean.create();
        server.getEndpoint().getInInterceptors().add(new SensorEventAuthenticationInterceptor(jwtAuthentication));
        server.getEndpoint().getInInterceptors().add(new SensorEventInterceptor());
        server.getEndpoint().getInInterceptors().add(new SensorConfigAuthenticationInterceptor(sensorConfigDS, securityService));
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
