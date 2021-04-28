package io.github.vm.patlego.iot.process;

public enum MThreadState {
    // MThread is currently executing
    RUNNING,

    // MThread thread is created but not running
    INITIALIZED,

    // Mthread had executed but is now done
    STOPPED,

    // MThread ran and then failed
    FAILED
}