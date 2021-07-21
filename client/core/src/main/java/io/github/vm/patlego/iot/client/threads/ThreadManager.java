package io.github.vm.patlego.iot.client.threads;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.vm.patlego.iot.client.MThread;
import io.github.vm.patlego.iot.client.config.Config;
import io.github.vm.patlego.iot.client.config.ConfigReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ThreadManager {

    // Sleep for 5 minutes
    protected int sleep = 300000;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Map<String, MThreadDTO> init(ConfigReader configReader) throws Exception {
        Map<String, MThreadDTO> threads = new ConcurrentHashMap<>();
        List<Config> configs = configReader.getConfigs();
        for (Config config : configs) {
            if (threads.get(config.getThread()) == null) {
                Class<MThread> mThreadClass = (Class<MThread>) Class.forName(config.getThread());
                Constructor<MThread> mThreadConstructor = mThreadClass.getConstructor(Config.class);
                MThread mThread = mThreadConstructor.newInstance(config);

                MThreadDTO mThreadDTO = new MThreadDTO();
                mThreadDTO.setmThread(mThread);

                threads.put(config.getThread(), mThreadDTO);
            } else {
                logger.error("The {} thread is already loaded skipping its instantiation since it is currently ",
                        config.getThread());
            }
        }

        return threads;
    }

    protected void run(ConfigReader configReader) throws Exception {
        try {
            Map<String, MThreadDTO> threads = this.init(configReader);
            while (true) {
                List<Config> configs = configReader.getConfigs();
                for (Map.Entry<String, MThreadDTO> entry : threads.entrySet()) {
                    MThreadDTO mThreadDTO = entry.getValue();
                    manageMThreadDTO(mThreadDTO, configs);
                }

                // Sleep 5 minutes
                Thread.sleep(this.sleep);
            }
        } catch (InterruptedException e) {
            logger.error("Thread has been interrupted most likely due to the thread being disabled or the system is turning down");
            Thread.currentThread().interrupt();
        }
    }

    protected void manageMThreadDTO(MThreadDTO mThreadDTO, List<Config> configs) {
        Config config = configs.stream().filter(c -> c.getModule().equals(mThreadDTO.getmThread().getModule())).findFirst().orElse(null);
        if (config != null) {
            if (config.isEnabled() && (mThreadDTO.getmThread().getState().equals(MThreadState.INITIALIZED)
                    || mThreadDTO.getmThread().getState().equals(MThreadState.STOPPED))) {
                Thread thread = new Thread(mThreadDTO.getmThread());
                mThreadDTO.setThread(thread);
                thread.start();
            }

            if (!config.isEnabled() && mThreadDTO.getmThread().getState().equals(MThreadState.RUNNING)) {
                mThreadDTO.getThread().interrupt();
            }
        }
    }

}
