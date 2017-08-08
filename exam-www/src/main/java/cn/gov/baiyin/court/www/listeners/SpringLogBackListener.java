package cn.gov.baiyin.court.www.listeners;

import cn.gov.baiyin.court.core.log.SpringLogBackFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by WK on 2017/3/24.
 */
public class SpringLogBackListener implements ServletContextListener {
    private static final String LOGFACTORY_KEY = "org.apache.commons.logging.LogFactory";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty(LOGFACTORY_KEY, SpringLogBackFactory.class.getName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //System.setProperty(LOGFACTORY_KEY, null);
    }
}
