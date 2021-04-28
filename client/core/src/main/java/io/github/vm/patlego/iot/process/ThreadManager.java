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

    private Map<String, ThreadDTO> threads = new HashMap<>();

    private String path;
    private ConfigFile configFile;

    public ThreadManager(String path) throws IOException {
        this.path = path;
        this.configFile = this.readFile(MainConfigFile.class);
    }
    
    private void init() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Config config : configFile.getConfigs()) {
            Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread());
            Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);

            MThread mThread = mThreadConstructor.newInstance(config);

            ThreadDTO mThreadDTO = new ThreadDTO();
            mThreadDTO.setThread(mThread);

            threads.put(config.getThread(), mThreadDTO);
        }
    }

    private ConfigFile readFile(Class<? extends ConfigFile> clazz) throws IOException {
        return ConfigReader.getConfigFile(this.path, clazz);
    }

    /**
     * Determines if the system should be halted or not
     * @return Boolean False do not halt the system while True means to halt the system
     */
    private Boolean haltSystem() {
        return this.configFile.haltSystem();
    }

    public void run() {
        while (Boolean.TRUE.equals(!this.haltSystem())) {

        }
    }
    
}
