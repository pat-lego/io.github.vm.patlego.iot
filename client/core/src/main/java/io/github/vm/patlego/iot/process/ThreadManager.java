package io.github.vm.patlego.iot.process;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public ThreadManager(String path) throws IOException {
        this.path = path;
        this.clazz = MainConfigFile.class;
    }

    private void init() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        for (Config config : this.readFile(this.clazz).getConfigs()) {
            Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread());
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

    public void run() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InterruptedException {
        this.init();
        while (Boolean.TRUE.equals(!this.haltSystem())) {
            ConfigFile configFile = this.readFile(this.clazz);

            for (Map.Entry<String, MThreadDTO> entry : this.threads.entrySet()) {
                Config config = configFile.getConfig(entry.getValue().getmThread().getModule());
                if (config != null) {
                    if (config.isEnabled() && !(entry.getValue().getmThread().getState().equals(MThreadState.RUNNING)
                            || entry.getValue().getmThread().getState().equals(MThreadState.FAILED))) {
                        Thread thread = new Thread(entry.getValue().getmThread());
                        entry.getValue().setThread(thread);
                        thread.start();
                    }

                    if (!config.isEnabled() && entry.getValue().getmThread().getState().equals(MThreadState.RUNNING)) {
                        entry.getValue().getThread().interrupt();
                    }
                }
            }

            Thread.sleep(this.sleep);
        }
    }

}
