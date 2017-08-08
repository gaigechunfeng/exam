package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IUserService;
import cn.gov.baiyin.court.core.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WK on 2017/3/25.
 */
@Controller
public class IndexController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/front/login")
    @ResponseBody
    public Msg frontLogin(HttpServletRequest request, String name, String username, String idcard) {

        try {
            User user = userService.frontLogin(name, username, idcard);

            Authc.doLogin(request.getSession(), user);
            return Msg.success(user);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/back/login")
    @ResponseBody
    public Msg adminLogin(HttpServletRequest request, String username, String password) {

        try {
            User user = userService.login(username, password);

            Authc.doAdminLogin(request.getSession(), user);
            return Msg.success(user);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/back/changePwd")
    @ResponseBody
    public Msg changePwd(HttpServletRequest request, String oldPwd, String newPwd, String newPwda) {

        try {
            Object o = request.getSession().getAttribute(Authc.AUTH_ADMIN_KEY);
            if (o == null) {
                throw new ServiceException("尚未登录！");
            }
            userService.changePwd((User) o, oldPwd, newPwd, newPwda);

            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/back/logout")
    @ResponseBody
    public Msg adminLogout(HttpServletRequest request) {

        request.getSession().removeAttribute(Authc.AUTH_ADMIN_KEY);

        return Msg.SUCCESS;
    }

    @RequestMapping("/back/checkLogin")
    @ResponseBody
    public Msg adminCheckLogin(HttpServletRequest request) {

        Object o = request.getSession().getAttribute(Authc.AUTH_ADMIN_KEY);
        if (o == null) {
            return Msg.ERROR_NO_LOGIN;
        } else {
            return Msg.success(o);
        }
    }

    @RequestMapping("/front/checkLogin")
    @ResponseBody
    public Msg checkLogin(HttpServletRequest request) {

        Object o = request.getSession().getAttribute(Authc.AUTH_KEY);
        if (o == null) {
            return Msg.ERROR_NO_LOGIN;
        } else {
            Map<String, Object> m = new HashMap<>();
            m.put("user", o);
            m.put("time", System.currentTimeMillis());
            return Msg.success(m);
        }
    }

    @RequestMapping("/index")
    public String index() {

        return "index";
    }
}
