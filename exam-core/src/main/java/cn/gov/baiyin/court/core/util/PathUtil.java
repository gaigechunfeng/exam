package cn.gov.baiyin.court.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by WK on 2017/3/24.
 */
public class PathUtil {
    private static final String ENCODING = "utf8";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");

    public static String getHome(Class<?> cls) {

        String p = cls.getResource("").toString();

        try {
            p = URLDecoder.decode(p, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decode error", e);
        }

        if (p.startsWith("file:")) {
            p = p.substring(5, p.indexOf("/target"));
        } else if (p.startsWith("jar:file:")) {
            p = p.substring(9, p.indexOf("/lib"));
        } else {
            throw new RuntimeException("cannot find home path");
        }
        if (IS_WINDOWS && p.startsWith("/")) {
            p = p.substring(1);
        }
        return p;
    }
}
