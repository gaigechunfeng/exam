package cn.gov.baiyin.court.core.authc;

import cn.gov.baiyin.court.core.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by WK on 2017/3/26.
 */
public class Authc {
    public static final String AUTH_KEY = "_auth_key_";
    public static final String AUTH_ADMIN_KEY = "_auth_admin_key_";

    private static final ThreadLocal<String> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void doAdminLogin(HttpSession session, User user) {

        session.setAttribute(AUTH_ADMIN_KEY, user);
    }

    public static void setCurrUserName(String username) {
        USER_THREAD_LOCAL.set(username);
    }

    public static String getCurrUserName() {

        return USER_THREAD_LOCAL.get();
    }

    public static void doLogin(HttpSession session, User user) {
        session.setAttribute(AUTH_KEY, user);
    }
}
