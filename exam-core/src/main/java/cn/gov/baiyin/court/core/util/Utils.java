package cn.gov.baiyin.court.core.util;

/**
 * Created by WK on 2017/3/27.
 */
public class Utils {
    private static PropertyHolder app;

    public static void setApp(PropertyHolder propertyHolder) {
        app = propertyHolder;
    }

    public static PropertyHolder getApp() {
        return app;
    }
}
