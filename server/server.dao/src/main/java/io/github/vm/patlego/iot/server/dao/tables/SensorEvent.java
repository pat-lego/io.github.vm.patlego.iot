package io.github.vm.patlego.iot.server.dao.tables;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "sensor_event")
@Table(name = "sensor_event", schema = "patlegovm")
public class SensorEvent {
    
    @Id
    @Column(name = "sensor_id", nullable = false, unique = true)
    private long sensorId;

    @Column(name = "time", nullable = false, unique = false)
    private Timestamp time;

    @Column(name = "location", nullable = false, unique = false)
    private String location;

    @Column(name = "type", nullable = false, unique = false)
    private String type;

    @Column(name = "thread", nullable = false, unique = false)
    private String thread;

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    

}
