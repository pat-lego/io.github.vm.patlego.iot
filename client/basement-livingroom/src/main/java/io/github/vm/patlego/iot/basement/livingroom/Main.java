package io.github.vm.patlego.iot.basement.livingroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.client.threads.ThreadManager;

public class Main extends ThreadManager {
    

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("About to initialize the Thread Manager");
        Main main = new Main();

        logger.info("Thread Manager successfully initialized and about to be executed");
        main.run(new BasementConfigReader());
    }
}
