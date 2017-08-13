package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineService;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/26.
 */
@Controller
@RequestMapping("/back/topic")
public class TopicController {

    private ITopicService topicService;
    private IExamineService examineService;

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

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

        Examine examine = examineService.findById(eid);
        if (examine.getType() == 1) {
            List<Topic> topics = topicService.listFieldsByEid(eid);
            return Msg.success(topics.stream().map(Topic::getName).collect(Collectors.toList()));
        } else {
            List<String> ss = new ArrayList<>();
            ss.add("\u5bf9\u7167\u9644\u5f55");
            ss.add("\u542c\u97f3\u6253\u5b57");
            return Msg.success(ss);
        }
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

    @RequestMapping("/delMulti")
    @ResponseBody
    public Msg delMulti(Integer[] ids) {

        try {
            topicService.delMulti(ids);
        } catch (ServiceException e) {
            return Msg.success(e.getMessage());
        }
        return Msg.SUCCESS;
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

    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response) throws ServiceException {

        File exported = topicService.exportAll();

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + exported.getName());
        try (FileInputStream fis = new FileInputStream(exported)) {

            StreamUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("download score excel error", e);
        }
    }
}
