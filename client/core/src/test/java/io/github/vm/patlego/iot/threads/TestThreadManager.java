package io.github.vm.patlego.iot.threads;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.opentest4j.AssertionFailedError;

public class TestThreadManager {

    @Test
    public void simpleTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            IOException, InterruptedException {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            ThreadManager manager = new ThreadManager("src/test/resources/simplethreads.json", new MyClassLoader());
            manager.run(10);
        });
    }

    @Test
    public void simpleTest_fail() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            IOException, InterruptedException {

        assertThrows(AssertionFailedError.class, () -> {
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
                ThreadManager manager = new ThreadManager("src/test/resources/simplethreads.json", new MyClassLoader());
                manager.run(0);
            });
        });

    }

    public class MyClassLoader extends ClassLoader {
        @Override
        public Class loadClass(String name) throws ClassNotFoundException {
            if (name.contains("App1")) {
                return App1.class;
            }

            return App2.class;
        }

    }

}
