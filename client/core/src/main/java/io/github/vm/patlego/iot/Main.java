package io.github.vm.patlego.iot;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Main {

    private static final String CONFIG_PATH = "--config";
    public static void main(String[] args) {
      String path = Main.getConfigFilePath(args);
    }

    public static String getConfigFilePath(String[] args) {
        List<String> argv = Arrays.asList(args);
        
        if(!argv.contains(CONFIG_PATH)) {
            throw new IllegalArgumentException("Missing  --config as part of the system startup");
        }

        Integer index = argv.indexOf(CONFIG_PATH);
        if (index < argv.size() - 1) {
            return argv.get(index + 1);
        } else {
            throw new IndexOutOfBoundsException("Missing config path within system startup");
        }
    }
}
