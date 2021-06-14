package io.github.vm.patlego.iot.client.threads;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class TestThreadManager {

    @Test
    public void simpleTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            IOException, InterruptedException {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            ThreadManager manager = new ThreadManager("src/test/resources/10_simplethreads.json", new MyClassLoader());
            manager.run();
        });
    }

    @Test
    public void simpleTest_fail() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            IOException, InterruptedException {

        assertThrows(AssertionFailedError.class, () -> {
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
                ThreadManager manager = new ThreadManager("src/test/resources/0_simplethreads.json", new MyClassLoader());
                manager.run();
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
