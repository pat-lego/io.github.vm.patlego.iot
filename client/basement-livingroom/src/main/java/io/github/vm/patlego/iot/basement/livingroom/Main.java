package io.github.vm.patlego.iot.basement.livingroom;

import io.github.vm.patlego.iot.client.threads.ThreadManager;

public class Main extends ThreadManager {
    
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.run(new BasementConfigReader());
    }
}
