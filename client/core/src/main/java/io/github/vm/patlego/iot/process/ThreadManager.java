package io.github.vm.patlego.iot.process;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.github.vm.patlego.iot.MThread;
import io.github.vm.patlego.iot.config.Config;
import io.github.vm.patlego.iot.config.ConfigFile;

public class ThreadManager {

    private Boolean IS_RUNNING = Boolean.FALSE;
    private Map<String, ThreadDTO> threads = new HashMap<>();
    
    public void init(ConfigFile configFile) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Config config : configFile.getConfigs()) {
            Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread());
            Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);

            MThread mThread = mThreadConstructor.newInstance(config);

            ThreadDTO mThreadDTO = new ThreadDTO();
            mThreadDTO.setThread(mThread);

            threads.put(config.getThread(), mThreadDTO);
        }
    }
    
}
