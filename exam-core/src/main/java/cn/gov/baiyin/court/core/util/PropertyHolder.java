package cn.gov.baiyin.court.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by WK on 2017/3/24.
 */
public class PropertyHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyHolder.class);

    private Properties properties = new Properties();

    public PropertyHolder(String filePath) {

        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException e) {
            LOGGER.error("load file error", e);
        }
    }

    public String getString(String s, String s1) {

        String val = properties.getProperty(s);
        if (StringUtils.isEmpty(val)) {
            return s1;
        }

        return val;
    }

    public int getInt(String s, int i) {

        String val = properties.getProperty(s);
        if (!StringUtils.isEmpty(val)) {
            try {
                return Integer.parseInt(val);
            } catch (Exception e) {
                LOGGER.error("parse int error", e);
            }
        }

        return i;
    }
}
