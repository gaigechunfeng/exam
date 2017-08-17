package cn.gov.baiyin.court.core.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WK on 2017/3/27.
 */
public class Utils {
    private static PropertyHolder app;
    private static String home;
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void setApp(PropertyHolder propertyHolder) {
        app = propertyHolder;
    }

    public static PropertyHolder getApp() {
        return app;
    }

    public static void setHome(String home) {
        Utils.home = home;
    }

    public static String getHome() {
        return home;
    }

    public static int nextNum() {
        return atomicInteger.getAndIncrement();
    }
}
