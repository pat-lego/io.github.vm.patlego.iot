package io.github.vm.patlego.iot.relay;

public class RelayException extends Exception {
    
    public RelayException(String msg, Throwable t) {
        super(msg, t);
    }

    public RelayException(String msg) {
        super(msg);
    }

    public RelayException(Throwable t) {
        super(t);
    }
}
