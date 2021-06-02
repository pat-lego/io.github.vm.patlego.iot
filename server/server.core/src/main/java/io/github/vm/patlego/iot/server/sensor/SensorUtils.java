package io.github.vm.patlego.iot.server.sensor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class SensorUtils {

    private SensorUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-LLLL-dd HH:mm:ss");

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String time(Timestamp time) {
        return time.toLocalDateTime().format(DATE_TIME_FORMATTER);
    }
    
}
