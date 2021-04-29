package io.github.vm.patlego.iot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import java.util.List;

import io.github.vm.patlego.iot.threads.ThreadManager;

/**
 * Hello world!
 *
 */
public class Main {

    private static final String CONFIG_PATH = "--config";

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, InterruptedException {
        String path = Main.getConfigFilePath(args);
        ThreadManager manager = new ThreadManager(path, Main.class.getClassLoader());
        manager.run(0);
    }

    public static String getConfigFilePath(String[] args) {
        List<String> argv = Arrays.asList(args);

        if (!argv.contains(CONFIG_PATH)) {
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
