package io.github.vm.patlego.iot.threads;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import io.github.vm.patlego.iot.MThread;
import io.github.vm.patlego.iot.MainConfigFile;
import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigFile;
import io.github.vm.patlego.iot.config.ConfigReader;

public class ThreadManager {

    private Map<String, MThreadDTO> threads = new HashMap<>();

    private String path;
    private Class<? extends ConfigFile> clazz;

    // Sleep for 5 seconds
    private int sleep = 5000;

    private ClassLoader loader;

    public ThreadManager(String path, ClassLoader loader) {
        // Call the other constructor first
        this.path = path;
        this.clazz = MainConfigFile.class;
        this.loader = loader;
    }

    private void init() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        for (Config config : this.readFile(this.clazz).getConfigs()) {
            Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread(), true, loader);
            Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);
            MThread mThread = mThreadConstructor.newInstance(config);

            MThreadDTO mThreadDTO = new MThreadDTO();
            mThreadDTO.setmThread(mThread);

            threads.put(config.getThread(), mThreadDTO);
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
     * @param timeout Stop the system after a certain amount of time
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public void run(long timeout) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            IOException, InterruptedException {
        Instant start = Instant.now();
        this.init();

        while (Boolean.TRUE.equals(!this.haltSystem()) && hasTimeoutElapsed(start, Instant.now(), timeout)) {
            ConfigFile configFile = this.readFile(this.clazz);

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

    private boolean hasTimeoutElapsed(Instant start, Instant end, long timeout) {
        if (this.validTime(timeout) == 0) {
            return Boolean.TRUE;
        }

        return (Duration.between(start, end).toSeconds() < timeout);
    }

    private long validTime(long time) {
        if (time <= 0) {
            return 0;
        }

        return time;
    }

}
