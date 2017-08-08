package cn.gov.baiyin.court.core.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Created by gaige on 2017/4/5.
 */
public class VelocityUtil {

    private static final String ENCODING = "utf8";
    private static VelocityEngine engine;

    static {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

        engine = new VelocityEngine(properties);
    }

    public static String parse(String tplName, Map<String, Object> context) {


        VelocityContext ctx = new VelocityContext(context);
        StringWriter sw = new StringWriter();
        engine.mergeTemplate("tpl/" + tplName + ".vm", ENCODING, ctx, sw);

        return sw.toString();
    }
}
