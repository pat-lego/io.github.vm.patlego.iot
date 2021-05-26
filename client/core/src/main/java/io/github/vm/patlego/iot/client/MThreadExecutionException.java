package io.github.vm.patlego.iot.client;

public class MThreadExecutionException extends Exception {
    public MThreadExecutionException(String msg, Throwable t) {
        super(msg, t);
    }

    public MThreadExecutionException(Throwable t) {
        super(t);
    }

    public MThreadExecutionException(String msg) {
        super(msg);
    }
}
