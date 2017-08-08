package cn.gov.baiyin.court.core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by WK on 2017/3/24.
 */
public class SpringLogBackFactory extends LogFactory {
    @Override
    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return new MyLog(LoggerFactory.getLogger(clazz));
    }

    @Override
    public Log getInstance(String name) throws LogConfigurationException {
        return new MyLog(LoggerFactory.getLogger(name));
    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAttribute(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAttribute(String name, Object value) {
        // TODO Auto-generated method stub

    }
}

class MyLog implements Log {

    private Logger logger;

    public MyLog() {
    }

    public MyLog(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void trace(Object message) {
        logger.trace(message.toString());
    }

    @Override
    public void trace(Object message, Throwable t) {
        logger.trace(message.toString(), t);
    }

    @Override
    public void debug(Object message) {
        logger.debug(message.toString());
    }

    @Override
    public void debug(Object message, Throwable t) {
        logger.debug(message.toString(), t);
    }

    @Override
    public void info(Object message) {
        logger.info(message.toString());
    }

    @Override
    public void info(Object message, Throwable t) {
        logger.info(message.toString(), t);
    }

    @Override
    public void warn(Object message) {
        logger.warn(message.toString());
    }

    @Override
    public void warn(Object message, Throwable t) {
        logger.warn(message.toString(), t);
    }

    @Override
    public void error(Object message) {
        logger.error(message.toString());
    }

    @Override
    public void error(Object message, Throwable t) {
        logger.error(message.toString(), t);
    }

    @Override
    public void fatal(Object message) {
        logger.error(message.toString());
    }

    @Override
    public void fatal(Object message, Throwable t) {
        logger.error(message.toString(), t);
    }

}
