package org.example.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class LogUtil {

    private final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public void info(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void warn(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void error(Class<?> clazz, String message, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }

    public void debug(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
