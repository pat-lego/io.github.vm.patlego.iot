package io.github.vm.patlego.iot.threads;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.MThread;
import io.github.vm.patlego.iot.MainConfigFile;
import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigFile;
import io.github.vm.patlego.iot.config.ConfigReader;

public class ThreadManager {

    private Map<String, MThreadDTO> threads = new HashMap<>();
    private String path;
    private Class<? extends ConfigFile> clazz;
    private ClassLoader loader;
    private Logger logger;

    // Sleep for 5 seconds
    private int sleep = 5000;

    public ThreadManager(String path, ClassLoader loader) throws IOException {
        this.path = path;
        this.clazz = MainConfigFile.class;
        this.loader = loader;
        this.loadLogger();
    }

    private void loadLogger() throws IOException {
        if (this.logger == null) {
            this.logger = this.readFile(this.clazz).getLogConfig().getLogger();
        }
    }

    private void init() throws IOException {
        ConfigFile configFile = this.readFile(this.clazz);
        for (Config config : configFile.getConfigs()) {
            try {
                if (threads.get(config.getThread()) == null) {
                    Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread(), true, loader);
                    Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);
                    MThread mThread = mThreadConstructor.newInstance(config);

                    MThreadDTO mThreadDTO = new MThreadDTO();
                    mThreadDTO.setmThread(mThread);

                    threads.put(config.getThread(), mThreadDTO);
                } else {
                    this.logger.info("The {} thread is already loaded skipping its instantiation since it is currently ", config.getThread());
                }
            } catch (ClassNotFoundException e) {
                this.logger.error("Was not able to load {} thread", config.getThread());
                this.logger.error(e.getMessage(), e);
            } catch (NoSuchMethodException | SecurityException e) {
                this.logger.error("Was not able to construct the constructor for {} thread", config.getThread());
                this.logger.error(e.getMessage(), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException e) {
                this.logger.error("Was not able to instantiate the given {} thread", config.getThread());
                this.logger.error(e.getMessage(), e);
            }
        }
    }

    private ConfigFile readFile(Class<? extends ConfigFile> clazz) throws IOException {
        return ConfigReader.getConfigFile(this.path, clazz);
    }

    /**
     * Determines if the system should be halted or not
     * 
     * @return False do not halt the system; True to halt the system
     * @throws IOException
     */
    private Boolean haltSystem() throws IOException {
        return this.readFile(this.clazz).haltSystem();
    }

    /**
     * Manages all the threads in the system
     * 
     * @throws IOException - could not locate config file
     */
    public void run() throws IOException {
        try {
            Instant start = Instant.now();
            ConfigFile configFile = this.readFile(MainConfigFile.class);
            this.init();

            while (Boolean.TRUE.equals(!this.haltSystem()) && hasTimeoutElapsed(start, Instant.now(), configFile)) {
                configFile = this.readFile(this.clazz);

                for (Map.Entry<String, MThreadDTO> entry : this.threads.entrySet()) {
                    MThreadDTO mThreadDTO = entry.getValue();
                    manageMThreadDTO(configFile, mThreadDTO);
                }

                Thread.sleep(this.sleep);
            }
        } catch (InterruptedException e) {
            this.logger.error(
                    "Thread has been interrupted most likely due to the thread being disabled or the system is turning down",
                    e);
            Thread.currentThread().interrupt();
        }
    }

    private void manageMThreadDTO(ConfigFile configFile, MThreadDTO mThreadDTO) {
        Config config = configFile.getConfig(mThreadDTO.getmThread().getModule());
        if (config != null) {
            if (config.isEnabled() && mThreadDTO.getmThread().getState().equals(MThreadState.INITIALIZED)) {
                Thread thread = new Thread(mThreadDTO.getmThread());
                mThreadDTO.setThread(thread);
                thread.start();
            }

            if (!config.isEnabled() && mThreadDTO.getmThread().getState().equals(MThreadState.RUNNING)) {
                mThreadDTO.getThread().interrupt();
            }
        }
    }

    private boolean hasTimeoutElapsed(Instant start, Instant end, ConfigFile configFile) {
        if (this.validTime(configFile.getTimeout()) == 0) {
            return Boolean.TRUE;
        }

        return (Duration.between(start, end).toSeconds() < configFile.getTimeout());
    }

    private Long validTime(Long time) {
        if (time <= 0L) {
            return 0L;
        }

        return time;
    }

}
