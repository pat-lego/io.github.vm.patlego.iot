# Basement Living Room

PIR Sensor to detect motion in the basement of the house.

## Systemd Service

Sensor is enabled on boot using systemd service following [this](https://www.raspberrypi.org/documentation/linux/usage/systemd.md) article.

## Sensor Log

PIR Sensor Log `journalctl -u basement.service` or `journalctl -u basement.service -f` to follow the tail of the log.

Refer [here](`journalctl -u basement.service`) for some really good examples.