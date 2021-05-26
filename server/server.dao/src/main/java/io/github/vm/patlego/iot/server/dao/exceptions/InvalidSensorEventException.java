package io.github.vm.patlego.iot.server.dao.exceptions;

public class InvalidSensorEventException extends Exception {

    public InvalidSensorEventException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidSensorEventException(String msg) {
        super(msg);
    }
    
}
