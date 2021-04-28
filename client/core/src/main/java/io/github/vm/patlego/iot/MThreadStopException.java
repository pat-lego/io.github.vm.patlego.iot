package io.github.vm.patlego.iot;

public class MThreadStopException extends Exception {
    
    public MThreadStopException(String msg, Throwable t) {
        super(msg, t);
    }

    public MThreadStopException(String msg) {
        super(msg);
    }

    public MThreadStopException(Throwable t) {
        super(t);
    }
}