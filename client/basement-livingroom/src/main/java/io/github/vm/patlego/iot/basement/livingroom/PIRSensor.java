package io.github.vm.patlego.iot.basement.livingroom;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import io.github.vm.patlego.iot.client.MThread;
import io.github.vm.patlego.iot.client.MainConfigLog;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.threads.MThreadState;

public class PIRSensor extends MThread {

    private final GpioController gpio;
    private final GpioPinDigitalInput pin;

    private static final int PIN_PIR = 11; // PIN 18 = BCM 24

    public PIRSensor(Config config) {
        super(config, new MainConfigLog());
        this.gpio = GpioFactory.getInstance();
         pin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
    }

    @Override
    public void run() {
        this.configLog.getLogger().info("PIR initialization");
        this.state = MThreadState.RUNNING;

        try {
            pin.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                }
            });


            while(this.keepRunning()) {
                this.configLog.getLogger().info("About to sleep thread");
                Thread.sleep(1000);
            }


        } catch (Exception e) {
            this.state = MThreadState.FAILED;
            this.configLog.getLogger().error("Caught exception when trying to run the PIR Sensor - set the state to failed");
        } finally {
            this.configLog.getLogger().info("GPIO is shutting down");
            gpio.shutdown();
        }

        this.configLog.getLogger().info("Run complete");
    }

    @Override
    public String getModule() {
        return "Basement Living Room";
    }

}
