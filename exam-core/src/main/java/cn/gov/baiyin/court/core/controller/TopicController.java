package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WK on 2017/3/26.
 */
@Controller
@RequestMapping("/back/topic")
public class TopicController {

    private ITopicService topicService;

    @Autowired
    public void setTopicService(ITopicService topicService) {
        this.topicService = topicService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Msg list(PageInfo pageInfo) {

        return Msg.success(topicService.listPagination(pageInfo));
    }

    @RequestMapping("/add")
    @ResponseBody
    public Msg add(Topic topic) {

        try {
            topicService.add(topic);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/listFields")
    @ResponseBody
    public Msg listFields(Integer eid) {

        return Msg.success(topicService.listFieldsByEid(eid));
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Msg edit(Topic topic) {

        try {
            topicService.edit(topic);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Msg delete(String id) {

        try {
            topicService.deleteById(id);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Msg detail(String id) {

        try {
            return Msg.success(topicService.findById(id));
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/import")
    @ResponseBody
    public Msg importUser(MultipartFile file) {

        try {
            topicService.importTopics(file);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }
}
