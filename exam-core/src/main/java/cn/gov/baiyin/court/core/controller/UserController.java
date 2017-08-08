package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IUserService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WK on 2017/3/25.
 */
@Controller
@RequestMapping("/back/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/listExaminee")
    @ResponseBody
    public Msg listExaminee(PageInfo pageInfo) {

        return Msg.success(userService.listExaminee(pageInfo));
    }

    @RequestMapping("/add")
    @ResponseBody
    public Msg add(User user, String examineIds) {

        try {
            userService.add(user, examineIds);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Msg edit(User user, String examineIds) {

        try {
            userService.edit(user, examineIds);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Msg detail(String id) {

        try {
            return Msg.success(userService.findById(id));
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Msg delete(String id) {

        try {
            userService.deleteById(id);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }

    @RequestMapping("/delMulti")
    @ResponseBody
    public Msg delMulti(Integer[] ids) {

        try {
            userService.deleteMulti(ids);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }

    @RequestMapping("/import")
    @ResponseBody
    public Msg importUser(MultipartFile file) {

        try {
            userService.importUser(file);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }
}
