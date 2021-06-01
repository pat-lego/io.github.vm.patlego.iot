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

import io.github.vm.patlego.iot.server.dao.exceptions.InvalidSensorEventException;
import io.github.vm.patlego.iot.server.dao.repo.SensorEventDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;
import io.github.vm.patlego.iot.server.sensor.SensorService;
import io.github.vm.patlego.iot.server.sensor.SensorServletPath;
import io.github.vm.patlego.sms.sender.SMSService;
import io.github.vm.patlego.sms.sender.bean.SMSMessage;

@Path(SensorServletPath.SENSOR_PATH)
public class SensorServiceServlet implements SensorService {

    private SensorEventDS sensorEventDS;
    private SMSService smsService;

    public SensorServiceServlet(SensorEventDS sensorEventDS, SMSService smsService) {
        this.sensorEventDS = sensorEventDS;
        this.smsService = smsService;
    }

    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @Override
    public SensorEvent createSensorEvent(SensorEvent event) {

        event.setTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/New_York"))));
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
