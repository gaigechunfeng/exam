package cn.gov.baiyin.court.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by WK on 2017/3/25.
 */
public class Msg {

    private static final Logger LOGGER = LoggerFactory.getLogger(Msg.class);
    public static final Msg SUCCESS = new Msg(true, null, null);
    public static final Msg ERROR_NO_RESULT = new Msg(false, "操作结果为0！", null);
    public static final Msg ERROR_NO_LOGIN = new Msg(false, "尚未登录！", null);
    boolean success;
    String errmsg;
    Object obj;

    public Msg(boolean success, String errmsg, Object obj) {
        this.success = success;
        this.errmsg = errmsg;
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static Msg success(Object o) {
        return new Msg(true, null, o);
    }

    public static Msg error(Exception e) {

        LOGGER.error(e.getMessage(), e);
        return new Msg(false, e.getMessage(), null);
    }
}
