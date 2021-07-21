package io.github.vm.patlego.iot.server.dao.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.github.vm.patlego.iot.server.dao.tables.config.Config;

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

    @Column(name = "sensor_key", nullable = false)
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        String parsed = "[ \n" +
                            "Id : %d \n" +
                            "Config: %s \n" +
                            "Key: %s \n" +
                        "]";
        return String.format(parsed, configId, config, key);
    }

    public static Config toConfig(String config) {
        return new GsonBuilder().create().fromJson(config, MainConfig.class);
    }

    public static String toString(Config config) {
        return new GsonBuilder().create().toJson(config);
    }

}
