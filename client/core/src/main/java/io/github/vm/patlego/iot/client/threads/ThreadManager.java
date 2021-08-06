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

    protected int sleep = 1000;

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
            logger.info("About to initialize the thread map");
            Map<String, MThreadDTO> threads = this.init(configReader);

            logger.info("Thread map initialized about to start thread invocation");
            while (true) {
                for (Map.Entry<String, MThreadDTO> entry : threads.entrySet()) {
                    MThreadDTO mThreadDTO = entry.getValue();
                    manageMThreadDTO(mThreadDTO);
                }
                Thread.sleep(this.sleep);
            }
        } catch (InterruptedException e) {
            logger.error(
                    "Thread has been interrupted most likely due to the thread being disabled or the system is turning down");
            Thread.currentThread().interrupt();
        }
    }

    protected void manageMThreadDTO(MThreadDTO mThreadDTO) {
        if (!mThreadDTO.getmThread().getState().equals(MThreadState.RUNNING)) {
            logger.info(String.format("The %s thread has received a %s MThreadState going to restart the thread", mThreadDTO.getmThread().getClassPath(), mThreadDTO.getmThread().getState().name()));
            Thread thread = new Thread(mThreadDTO.getmThread());
            mThreadDTO.setThread(thread);
            thread.start();
        }
    }

}
