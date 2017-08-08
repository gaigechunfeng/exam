package cn.gov.baiyin.court.core.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by WK on 2017/3/25.
 */
public class HashUtil {
    private static final String ENCODING = "UTF-8";

    public static String md5(String str) {
        try {
            return DigestUtils.md5DigestAsHex(str.getBytes(ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("md5 error", e);
        }
    }
}
