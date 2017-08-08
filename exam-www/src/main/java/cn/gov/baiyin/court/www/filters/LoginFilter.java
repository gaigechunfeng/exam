package cn.gov.baiyin.court.www.filters;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by WK on 2017/3/26.
 */
public class LoginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    private String frontLoginPage;
    private String backLoginPage;
    private String[] staticPaths;
    private String[] noNeedLoginPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        frontLoginPage = filterConfig.getInitParameter("frontLoginPage");
        backLoginPage = filterConfig.getInitParameter("backLoginPage");

        if (StringUtils.isEmpty(frontLoginPage) || StringUtils.isEmpty(backLoginPage)) {
            throw new RuntimeException("login filter init params not set correctly~~ need [frontLoginPage,backLoginPage]");
        }

        String sp = filterConfig.getInitParameter("staticPaths");
        if (!StringUtils.isEmpty(sp)) {
            staticPaths = sp.split(",|;");
        }

        String np = filterConfig.getInitParameter("noNeedLoginPaths");
        if (!StringUtils.isEmpty(np)) {
            noNeedLoginPaths = np.split(",|;");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

//        HttpSession session = req.getSession();

        if (needLogin(req) && !hasLogin(req)) {
            toLogin(req, res);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 是否需要登录
     *
     * @param req
     * @return
     */
    private boolean needLogin(HttpServletRequest req) {
        return !isStatic(req) && !isLoginPage(req) && !specialExcept(req);
    }

    /**
     * web.xml配置的不需要登录的页面
     *
     * @param req
     * @return
     */
    private boolean specialExcept(HttpServletRequest req) {

        String requestPath = req.getRequestURI();
        if (noNeedLoginPaths != null) {
            for (String noNeedLoginPath : noNeedLoginPaths) {

                if (requestPath.startsWith(req.getContextPath() + noNeedLoginPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否静态资源
     *
     * @param req
     * @return
     */
    private boolean isStatic(HttpServletRequest req) {
        String requestPath = req.getRequestURI();

        for (String staticPath : staticPaths) {
            if (requestPath.startsWith(req.getContextPath() + "/front/" + staticPath + "/")
                    || requestPath.startsWith(req.getContextPath() + "/back/" + staticPath + "/")) {
                return true;
            }
        }
        return false;
    }

    private void toLogin(HttpServletRequest req, HttpServletResponse res) {
        String requestPath = req.getRequestURI();
        boolean isBack = requestPath.contains(req.getContextPath() + "/back/");

        try {
            if (isBack) {
                res.sendRedirect(req.getContextPath() + "/back/" + backLoginPage);
            } else {
                res.sendRedirect(req.getContextPath() + "/front/" + frontLoginPage);
            }
        } catch (Exception e) {
            throw new RuntimeException("toLogin error~", e);
        }
    }

    private boolean hasLogin(HttpServletRequest req) {

        String requestPath = req.getRequestURI();
        boolean isBack = requestPath.contains(req.getContextPath() + "/back/");
        String key = isBack ? Authc.AUTH_ADMIN_KEY : Authc.AUTH_KEY;

        Object o = req.getSession().getAttribute(key);
        boolean hasLogin = o != null && o instanceof User;

        if (hasLogin) {
            Authc.setCurrUserName(((User) o).getUsername());
        }
        return hasLogin;
    }

    /**
     * 是否是登录页面
     *
     * @param req
     * @return
     */
    private boolean isLoginPage(HttpServletRequest req) {

        String requestPath = req.getRequestURI();

        return requestPath.contains("front/" + frontLoginPage) || requestPath.contains("back/" + backLoginPage);
    }

    @Override
    public void destroy() {

        frontLoginPage = null;
        backLoginPage = null;
    }
}
