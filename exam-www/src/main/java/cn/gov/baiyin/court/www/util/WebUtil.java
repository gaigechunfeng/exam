package cn.gov.baiyin.court.www.util;


import cn.gov.baiyin.court.core.util.PathUtil;
import cn.gov.baiyin.court.core.util.PropertyHolder;

/**
 * Created by WK on 2017/3/24.
 */
public class WebUtil {

    public static final String HOME = PathUtil.getHome(WebUtil.class);
    public static final PropertyHolder APP = new PropertyHolder(HOME+"/conf/app.properties");
}
