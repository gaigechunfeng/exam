package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by WK on 2017/3/26.
 */
@Controller
@RequestMapping("/back/examine")
public class ExamineController {

    private IExamineService examineService;

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Msg list(PageInfo pageInfo) {

        return Msg.success(examineService.listPagination(pageInfo));
//        return examineService.listPagination(pageInfo);
    }

    @RequestMapping("/add")
    @ResponseBody
    public Msg add(Examine examine, String topicIds, String esessionIds) {

        try {
            examineService.add(examine, topicIds, esessionIds);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/changeKsxz")
    @ResponseBody
    public Msg changeKsxz(String ksxz, String kssm) {

        try {
            examineService.changeKsxz(ksxz, kssm);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/showKsxz")
    @ResponseBody
    public Msg showKsxz() {

        return Msg.success(examineService.findKsxz());
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Msg edit(Examine examine, String topicIds, String esessionIds) {

        try {
            examineService.edit(examine, topicIds, esessionIds);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Msg detail(String id) {

        try {
            Map<String, Object> data = examineService.detail(id);
            return Msg.success(data);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Msg delete(String id) {

        try {
            examineService.deleteById(id);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }
}
