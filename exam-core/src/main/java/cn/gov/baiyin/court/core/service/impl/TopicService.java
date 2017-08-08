package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.ITopicDAO;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineTopicService;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/26.
 */
@Service
public class TopicService implements ITopicService {

    private ITopicDAO topicDAO;
    private IExamineTopicService examineTopicService;

    @Autowired
    public void setExamineTopicService(IExamineTopicService examineTopicService) {
        this.examineTopicService = examineTopicService;
    }

    @Autowired
    public void setTopicDAO(ITopicDAO topicDAO) {
        this.topicDAO = topicDAO;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {
        return topicDAO.listPagination(pageInfo);
    }

    @Override
    public void add(Topic topic) throws ServiceException {

        if (StringUtils.isEmpty(topic.getName()) || StringUtils.isEmpty(topic.getContent())) {
            throw new ServiceException("试题名称和内容不能为空！");
        }

        if (topic.getType() == Topic.Type.LISTEN.getTypeVal()) {
            //将临时文件夹中的文件拷贝到正式资料文件夹
            try {
                moveAndSet(topic);
            } catch (java.io.IOException e) {
                throw new ServiceException("copy file from temp dir to production dir error", e);
            }
        }

        boolean r = topicDAO.add(topic);
        if (!r) {
            throw new ServiceException("保存试题失败！");
        }
    }

    @Override
    public void deleteById(String id) throws ServiceException {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误[id]");
        }

        int tid = Integer.parseInt(id);
        boolean r = topicDAO.deleteById(tid);
        if (!r) {
            throw new ServiceException("操作失败！");
        }

        examineTopicService.removeByTid(tid);
    }

    @Override
    public Topic findById(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！[id]");
        }

        return topicDAO.load(Integer.parseInt(id));
    }

    @Override
    public void edit(Topic topic) throws ServiceException {
        if (StringUtils.isEmpty(topic.getName()) || StringUtils.isEmpty(topic.getContent())) {
            throw new ServiceException("试题名称和内容不能为空！");
        }

        //如果原附件存在且现在没有附件或者现在的附件与原附件不同，，则删除原附件
        Topic originTopic = topicDAO.load(topic.getId());
        if (originTopic == null) {
            throw new ServiceException("entity not found![" + topic.getId() + "]");
        }
        if (originTopic.getType() == Topic.Type.LISTEN.getTypeVal()
                && !StringUtils.isEmpty(originTopic.getContent())
                && (topic.getType() != Topic.Type.LISTEN.getTypeVal() || !originTopic.getContent().equals(topic.getContent()))) {
            try {
                FileUtil.deleteFile(originTopic.getContent());
            } catch (IOException e) {
                throw new RuntimeException("remove origin file error!", e);
            }
        }

        if (topic.getType() == Topic.Type.LISTEN.getTypeVal() && FileService.isTempFile(topic.getContent())) {
            //将临时文件夹中的文件移动到正式资料文件夹
            try {
                moveAndSet(topic);
            } catch (java.io.IOException e) {
                throw new ServiceException("move file from temp dir to production dir error", e);
            }

        }

        BeanUtils.copyProperties(originTopic, topic, "name", "score", "type", "period", "content", "answer", "playtype");
        boolean r = topicDAO.edit(topic);
        if (!r) {
            throw new ServiceException("保存试题失败！");
        }
    }

    @Override
    public Topic findById(Integer tid) {
        return topicDAO.load(tid);
    }

    @Override
    public List<String> listFieldsByEid(Integer eid) {

        List<Topic> topics = topicDAO.findByEid(eid);
        return topics.stream().map(Topic::getName).collect(Collectors.toList());
    }

    private static void moveAndSet(Topic topic) throws IOException {
        File productionFile = FileUtil.moveTemp2Pro(topic.getContent());
        topic.setContent(productionFile.getAbsolutePath());
    }
}
