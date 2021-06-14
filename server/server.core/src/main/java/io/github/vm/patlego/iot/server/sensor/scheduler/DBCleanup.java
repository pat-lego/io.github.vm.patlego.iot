package io.github.vm.patlego.iot.server.sensor.scheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.dao.repo.SensorEventDS;

@Component(
        immediate = true,
        service = Runnable.class,
        property = {
                "scheduler.name=io.github.vm.patlego.iot.server.sensor.scheduler.DBCleanup",
                "scheduler.expression=0 0 0 ? * * *",
                "scheduler.concurrent:Boolean=false"
        }
)
public class DBCleanup implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private SensorEventDS sensorEventDS;

    @Override
    public void run() {
        logger.info("About to delete older records from basement");
        Instant now = LocalDateTime.now().minusDays(7).atZone(ZoneId.of("America/Montreal")).toInstant();
        int deleted = sensorEventDS.deleteEvent(Timestamp.from(now));
        logger.info(String.format("Deleted %d events from the DB", deleted));
    }
    
}
