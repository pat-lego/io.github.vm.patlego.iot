package io.github.vm.patlego.iot.server.sensor.servlets;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.authentication.Authentication;
import io.github.vm.patlego.iot.server.authentication.jwt.Jwt;
import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;
import io.github.vm.patlego.iot.server.dao.tables.config.Config;
import io.github.vm.patlego.iot.server.sensor.SensorConfigService;

import io.github.vm.patlego.iot.server.sensor.SensorServletPath;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path(SensorServletPath.SENSOR_CONFIGS)
public class SensorConfigsServiceServlet implements SensorConfigService {

    private Authentication<Jwt> authentication;
    private SensorConfigDS sensorConfigDS;
    private Security security;

    private static final String ISS = "patlego-vm IoT module";

    public SensorConfigsServiceServlet(Authentication authentication, SensorConfigDS sensorConfigDS, Security security) {
        this.authentication = authentication;
        this.sensorConfigDS = sensorConfigDS;
        this.security = security;
    }

    @Path("/{id}")
    @Produces("application/json")
    @GET
    @Override
    public Config getConfig(@PathParam("id") long id) {
        SensorConfig sensorConfig = this.sensorConfigDS.getConfig(id);

        if (sensorConfig == null) {
            throw new IllegalArgumentException("Invalid identifier used to look for Sensor Configs");
        }
        Config config = SensorConfig.toConfig(sensorConfig.getConfig());
        

        Jwt jwt = new Jwt();
        jwt.setIat(Date.from(LocalDateTime.now().atZone(ZoneId.of("America/New_York")).toInstant()));
        jwt.setExp(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.of("America/New_York")).toInstant()));
        jwt.setIss(ISS);
        jwt.setAud(config.getModule());
        jwt.setSub(config.getThread());

        String jwtToken = this.authentication.createToken(jwt);
        String encryptedToken = this.security.encrypt(jwtToken);

        config.getSystem().getAuth().setAuthorization(encryptedToken);

        return config;
    }
    
}
