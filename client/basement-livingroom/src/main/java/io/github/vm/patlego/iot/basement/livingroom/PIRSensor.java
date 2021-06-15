package io.github.vm.patlego.iot.basement.livingroom;

import java.time.Duration;
import java.time.Instant;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import org.apache.http.client.methods.CloseableHttpResponse;

import io.github.vm.patlego.iot.client.MThread;
import io.github.vm.patlego.iot.client.MainConfigLog;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.relay.Relay;
import io.github.vm.patlego.iot.client.relay.RelayException;
import io.github.vm.patlego.iot.client.relay.RelayInstantiationException;
import io.github.vm.patlego.iot.client.threads.MThreadState;

public class PIRSensor extends MThread {

    private GpioController gpio;
    private GpioPinDigitalInput pirSensor;
    private static final long SENSOR_TIMEOUT = 10000;
    private Boolean interrupt = false;

    public PIRSensor(Config config) {
        super(config, new MainConfigLog());
    }

    private void init() {
        this.gpio = GpioFactory.getInstance();
        pirSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
    }

    @Override
    public void run() {

        try {
            this.state = MThreadState.RUNNING;
        
            this.configLog.getLogger().info("Allowing PIR Sensor to warm up sensor before initiating listeners");
            Thread.sleep(120000);
            this.configLog.getLogger().info("Sensor has been configured and setup will proceed");

            // Incase the program stopped allow for a clean start
            init();

            pirSensor.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    try {
                        if (event.getState().isHigh()) {
                            Relay relay = getRelay(config.getSystem());
                            CloseableHttpResponse response = (CloseableHttpResponse) relay.execute(config, null);

                            if ((response.getStatusLine().getStatusCode() / 100) != 2) {
                                configLog.getLogger()
                                        .error(String.format("Received an error when invoking the relay for %s",
                                                config.getSystem().getRelay().getClassPath()));
                            } else {
                                configLog.getLogger().info(String.format("Successfully invoked the relay for %s",
                                        config.getSystem().getRelay().getClassPath()));
                            }
                        }
                    } catch (RelayException e) {
                        configLog.getLogger().error("Failed to submit event data to server", e);
                    } catch (RelayInstantiationException e) {
                        configLog.getLogger().error("Failed to instantiate relay - setting thread to FAILED", e);
                        state = MThreadState.FAILED;
                        interrupt = Boolean.TRUE;
                    } catch (Exception e) {
                        configLog.getLogger().error("Caught generic exception - setting thread to FAILED", e);
                        state = MThreadState.FAILED;
                        interrupt = Boolean.TRUE;
                    }
                }
            });

            while (Boolean.TRUE.equals(this.keepRunning()) && Boolean.FALSE.equals(interrupt)) {
                this.configLog.getLogger().info(String.format("About to sleep PIR Sensor for %d ms", SENSOR_TIMEOUT));
                Thread.sleep(SENSOR_TIMEOUT);
                this.configLog.getLogger()
                        .info(String.format("Waking up PIR Sensor after sleeping for %d ms", SENSOR_TIMEOUT));
            }
        } catch (InterruptedException e) {
            this.state = MThreadState.STOPPED;
            this.configLog.getLogger()
                    .error("Caught interruption when trying to run the PIR Sensor - set the state to stopped");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            this.state = MThreadState.FAILED;
            this.configLog.getLogger()
                    .error("Caught exception when trying to run the PIR Sensor - set the state to failed");
        } finally {
            this.configLog.getLogger().info("GPIO is shutting down");
            if (pirSensor != null) {
                pirSensor.removeAllListeners();
            }
            if (gpio != null) {
                gpio.shutdown();
            }
        }
    }

    @Override
    public String getModule() {
        return "Basement Living Room";
    }
}
