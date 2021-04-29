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
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // Sleep for 5 seconds
    private int sleep = 5000;


    public ThreadManager(String path, ClassLoader loader) {
        this.path = path;
        this.clazz = MainConfigFile.class;
        this.loader = loader;
    }

    private void init(ConfigFile configFile) throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        try {
            for (Config config : configFile.getConfigs()) {
                Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread(), true, loader);
                Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);
                MThread mThread = mThreadConstructor.newInstance(config);

                MThreadDTO mThreadDTO = new MThreadDTO();
                mThreadDTO.setmThread(mThread);

                threads.put(config.getThread(), mThreadDTO);
            }
        } catch (ClassNotFoundException e) {

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
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public void run() throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
            InterruptedException {
        Instant start = Instant.now();
        ConfigFile configFile = this.readFile(MainConfigFile.class);
        this.init(configFile);

        while (Boolean.TRUE.equals(!this.haltSystem()) && hasTimeoutElapsed(start, Instant.now(), configFile)) {
            configFile = this.readFile(this.clazz);

            for (Map.Entry<String, MThreadDTO> entry : this.threads.entrySet()) {
                MThreadDTO mThreadDTO = entry.getValue();
                manageMThreadDTO(configFile, mThreadDTO);
            }

            Thread.sleep(this.sleep);
        }
    }

    private void manageMThreadDTO(ConfigFile configFile, MThreadDTO mThreadDTO) {
        Config config = configFile.getConfig(mThreadDTO.getmThread().getModule());
        if (config != null) {
            if (config.isEnabled() && !(mThreadDTO.getmThread().getState().equals(MThreadState.RUNNING)
                    || mThreadDTO.getmThread().getState().equals(MThreadState.FAILED))) {
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
