package io.github.vm.patlego.iot.server.sensor.servlets;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;
import io.github.vm.patlego.iot.server.sensor.SensorService;
import io.github.vm.patlego.iot.server.sensor.SensorServletPath;

@Path(SensorServletPath.SENSOR_PATH)
public class SensorServiceServlet implements SensorService {

    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @Override
    public SensorEvent createSensorEvent(SensorEvent event) {
        // TODO Auto-generated method stub
        return null;
    }

    @Path("/{id}")
    @Produces("application/json")
    @GET
    @Override
    public SensorEvent getSensorEvent(@PathParam("id") long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Path("/")
    @Produces("application/json")
    @GET
    @Override
    public List<SensorEvent> getSensorEvents() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
