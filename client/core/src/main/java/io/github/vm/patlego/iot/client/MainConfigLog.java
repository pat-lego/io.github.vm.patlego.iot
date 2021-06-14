package io.github.vm.patlego.iot.client;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import io.github.vm.patlego.iot.client.config.ConfigLog;
import io.github.vm.patlego.iot.client.threads.ThreadManager;

public class MainConfigLog implements ConfigLog {

    private String name;
    private String path;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Logger getLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (this.name == null || this.name.isBlank() || this.path == null || this.path.isBlank()) {
            return loggerContext.getLogger(ThreadManager.class);
        }
        
        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setName(this.name);
        fileAppender.setFile(String.format("%s/%s.log", this.path, this.name));

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%r [%thread] %level - %msg%n");
        encoder.start();

        fileAppender.setEncoder(encoder);
        fileAppender.start();

        // attach the rolling file appender to the logger of your choice
        Logger logger = loggerContext.getLogger(this.name);
        logger.addAppender(fileAppender);
        return logger;
    }

}
