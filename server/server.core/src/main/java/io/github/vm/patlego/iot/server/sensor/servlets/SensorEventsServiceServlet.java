package io.github.vm.patlego.iot.server.sensor.servlets;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.cxf.interceptor.Fault;

import io.github.vm.patlego.enc.Security;
import io.github.vm.patlego.iot.server.dao.exceptions.InvalidSensorEventException;
import io.github.vm.patlego.iot.server.dao.repo.SensorEventDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;
import io.github.vm.patlego.iot.server.sensor.SensorEventService;
import io.github.vm.patlego.iot.server.sensor.SensorServletPath;
import io.github.vm.patlego.iot.server.sensor.SensorUtils;
import io.github.vm.patlego.sms.sender.SMSService;
import io.github.vm.patlego.sms.sender.bean.SMSMessage;

@Path(SensorServletPath.SENSOR_PATH)
public class SensorEventsServiceServlet implements SensorEventService {

    private SensorEventDS sensorEventDS;
    private SMSService smsService;
    private Security securityService;

    public SensorEventsServiceServlet(SensorEventDS sensorEventDS, SMSService smsService, Security securityService) {
        this.sensorEventDS = sensorEventDS;
        this.smsService = smsService;
        this.securityService = securityService;
    }

    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @Override
    public SensorEvent createSensorEvent(SensorEvent event) {
        event.setTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/New_York"))));
        SMSMessage smsMessage = new SMSMessage() {

            @Override
            public String getMessage() {
                String message = "%s captured motion at %s " + 
                                "Motion was captured at the %s location";
                return String.format(message, 
                            SensorUtils.capitalize(event.getType()), 
                            SensorUtils.time(event.getTime()), 
                            SensorUtils.capitalize(event.getLocation()));
            }

            @Override
            public Set<String> getNumbers() {
                return Set.of(securityService.decrypt("kTIFbeXaKvz6yyVWdHMZOX1dJn8EMA/N"));
            }

        };
        this.smsService.sendMessage(smsMessage);
        return this.sensorEventDS.createEvent(event);
    }

    @Path("/{id}")
    @Produces("application/json")
    @GET
    @Override
    public SensorEvent getSensorEvent(@PathParam("id") long id) {
        try {
            return sensorEventDS.getEvent(id);
        } catch (InvalidSensorEventException e) {
            throw new Fault(e);
        }

    }

    @Path("/")
    @Produces("application/json")
    @GET
    @Override
    public List<SensorEvent> getSensorEvents() {
        return this.sensorEventDS.getEvents();
    }
    
}
