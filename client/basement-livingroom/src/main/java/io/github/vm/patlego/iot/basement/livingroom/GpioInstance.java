package io.github.vm.patlego.iot.basement.livingroom;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class GpioInstance {

    private static GpioController gpio;

    private GpioInstance() {
        throw new IllegalStateException("Utility class");
      }
    
    public static final synchronized GpioController getInstance() {
        if (gpio == null) {
            gpio = GpioFactory.getInstance();
        }

        return gpio;
    }

    public static synchronized void destroyInstance() {
        if (gpio != null) {
            gpio.shutdown();
            gpio = null;
        }
    }
    
}
