package io.github.vm.patlego.iot.basement.livingroom;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

import org.apache.http.client.methods.CloseableHttpResponse;

import io.github.vm.patlego.iot.client.MThread;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.relay.Relay;
import io.github.vm.patlego.iot.client.threads.MThreadState;

public class PIRSensor extends MThread {

    private GpioController gpio;
    private GpioPinDigitalInput pirSensorPin;

    // Sleep for 10 seconds
    private static final long SENSOR_TIMEOUT = 10000;

    public PIRSensor(Config config) {
        super(config);
        this.state = MThreadState.INITIALIZED;
    }

    private void init() {
        this.gpio = GpioInstance.getInstance();
        pirSensorPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
    }

    @Override
    public void run() {
        try {
            this.state = MThreadState.RUNNING;

            // Incase the program stopped allow for a clean start
            init();

            logger.info(String.format("The config for the PiR sensor is set to %s with respect to the enabled field", this.keepRunning().toString()));
            
            while (Boolean.TRUE.equals(this.keepRunning())) {
                logger.info(String.format("About to sleep PIR Sensor for %d ms", SENSOR_TIMEOUT));
                if (pirSensorPin.isHigh()) {
                    Relay relay = getRelay(config.getSystem());
                    CloseableHttpResponse response = (CloseableHttpResponse) relay.execute(config, null);

                    if ((response.getStatusLine().getStatusCode() / 100) != 2) {
                        logger.error(String.format("Received an error when invoking the relay for %s",
                                config.getSystem().getRelay().getClassPath()));
                    } else {
                        logger.info(String.format("Successfully invoked the relay for %s",
                                config.getSystem().getRelay().getClassPath()));
                    }
                }
                Thread.sleep(SENSOR_TIMEOUT);
                logger.info(String.format("Waking up PIR Sensor after sleeping for %d ms", SENSOR_TIMEOUT));
            }
        } catch (InterruptedException e) {
            this.state = MThreadState.STOPPED;
            logger.error("Caught interruption when trying to run the PIR Sensor - set the state to stopped");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            this.state = MThreadState.FAILED;
            logger.error("Caught exception when trying to run the PIR Sensor - set the state to failed", e);
        } finally {
            logger.info("GPIO is shutting down");
            if (gpio != null) {
                gpio.unprovisionPin(pirSensorPin);
            }
        }
    }

    @Override
    public String getModule() {
        return this.config.getModule();
    }
}
