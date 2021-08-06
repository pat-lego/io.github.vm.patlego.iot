package io.github.vm.patlego.iot.server.commands.argument;

public class PropertyException extends Exception {

    public PropertyException(String msg, Throwable t) {
        super(msg, t);
    }

    public PropertyException(String msg) {
        super(msg);
    }
    
}
