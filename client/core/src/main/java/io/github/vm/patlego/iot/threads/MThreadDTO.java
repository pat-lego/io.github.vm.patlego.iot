package io.github.vm.patlego.iot.threads;

import io.github.vm.patlego.iot.MThread;

public class MThreadDTO {

    private MThread mThread;
    private Thread thread;

    public MThread getmThread() {
        return mThread;
    }

    public void setmThread(MThread mThread) {
        this.mThread = mThread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return this.thread;
    }
    
}
