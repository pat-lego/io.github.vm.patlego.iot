package io.github.vm.patlego.iot.server.commands;

import java.util.List;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.commands.argument.Property;
import io.github.vm.patlego.iot.server.commands.argument.PropertyException;
import io.github.vm.patlego.iot.server.commands.argument.SimpleProperty;
import io.github.vm.patlego.iot.server.dao.repo.SensorConfigDS;
import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;
import io.github.vm.patlego.iot.server.dao.tables.config.Config;

@Service
@Command(scope = "iot", name = "update-sensor-config", description = "Update data within the Sensor Confg")
public class UpdateSensorConfig implements Action {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Argument(index = 0, name = "configId", description = "The Sensor Config ID to update", required = true, multiValued = false)
    public long id = 0;

    @Argument(index = 1, name = "jsonPath", description = "Json Path for the given config. Note that the config must resolve to a singular property within the Json object", required = true, multiValued = false)
    public String jsonPath = null;

    @Argument(index = 1, name = "value", description = "The value to set at the given jsonPath prefix, if the jsonPath does not resolve then nothing is updated", required = true, multiValued = false)
    public String value = null;

    @Reference
    public SensorConfigDS sensorConfigDS;

    @Override
    public Object execute() throws Exception {
        Configuration conf = Configuration.builder().options(Option.ALWAYS_RETURN_LIST).build();
        SensorConfig sensorConfig = this.sensorConfigDS.getConfig(id);


        if (sensorConfig == null) {
            return "The given id did not return any results in the system";
        }

        List<String> results = JsonPath.using(conf).parse(sensorConfig.getConfig()).read(jsonPath);

        if (results.isEmpty()) {
            throw new Exception("The given json path did not locate any values to update within the json object");
        }

        if (results.size() > 1) {
            throw new Exception("The given json path located more then one option to update, please correct your query");
        }

        Object property = this.getValue(value);
        String updatedConfig = JsonPath.using(conf).parse(sensorConfig.getConfig()).set(jsonPath, property).jsonString();

        sensorConfig.setConfig(updatedConfig);
        this.sensorConfigDS.updateConfig(sensorConfig);

        return String.format("The sensor config with id %d has been successfully updated", id);
    }

    public Object getValue(String value) throws PropertyException {
        Property prop = new SimpleProperty(value);

        switch (prop.getType()) {
            case BOOLEAN:
                return prop.getAsBoolean();
            case INTEGER: 
                return prop.getAsInteger();
            case FLOAT:
                return prop.getAsFloat();
            case DOUBLE: 
                return prop.getAsDouble();
            default:
                return prop.getAsString();
        }
    }

}
