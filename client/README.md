# Client

The thread manager class offers the ability to run multiple sensors at once using various classes that are meant to be extended and provide HTTP access to external servers. 

## Build

Navigate to the root of client/ and execute

```
mvn clean install
```

## Classes

The `Relay` class can be used to send Http transactions to an external server were as the `MThread` class is used to execute operations locally with respect to a sensor and then can leverage the `Relay` class to send data externally.

## Run

Start the jar with the following command

```
java -jar client.core-<version>.jar
```

## Create systemd Service

In order to create a systemd service with the Raspberry Pi follow the given [documentation](https://www.raspberrypi.org/documentation/linux/usage/systemd.md).
