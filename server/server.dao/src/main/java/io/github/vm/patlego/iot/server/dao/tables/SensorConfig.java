package io.github.vm.patlego.iot.server.dao.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity(name = "sensor_configs")
@Table(name = "sensor_configs", schema = "patlegovm")
@TypeDef(
    name = "jsonb",
    typeClass = JsonBinaryType.class
)
public class SensorConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_config_id", nullable = false, unique = true)
    private long configId;

    @Type(type = "jsonb")
    @Column(name = "sensor_config", columnDefinition = "JSONB", nullable = false)
    private String config;

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    

}
