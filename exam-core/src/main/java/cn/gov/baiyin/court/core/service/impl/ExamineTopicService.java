package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.IExamineTopicDAO;
import cn.gov.baiyin.court.core.entity.ExamineTopic;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
@Service
public class ExamineTopicService implements IExamineTopicService {

    private IExamineTopicDAO examineTopicDAO;

    @Autowired
    public void setExamineTopicDAO(IExamineTopicDAO examineTopicDAO) {
        this.examineTopicDAO = examineTopicDAO;
    }

    @Override
    public ExamineTopic addByEidAndTid(int eid, int topicId) throws ServiceException {

        ExamineTopic examineTopic = findByEidAndTid(eid, topicId);
        if (examineTopic == null) {
            boolean r = examineTopicDAO.add(new ExamineTopic(eid, topicId));
            if (!r) {
                throw new ServiceException("执行结果为空！");
            }
            examineTopic = findByEidAndTid(eid, topicId);
        }
        return examineTopic;
    }

    @Override
    public void removeByEid(int eid) {
        examineTopicDAO.removeByEid(eid);
    }

    @Override
    public void removeByTid(int tid) {
        examineTopicDAO.removeByTid(tid);
    }

    @Override
    public ExamineTopic findByEidAndTid(int eid, int topicId) {
        return examineTopicDAO.findOne(eid, topicId);
    }

    @Override
    public List<Topic> findByEid(Integer id) {
        return examineTopicDAO.findByEid(id);
    }

    @Override
    public void removeRelationByEidAndTid(Integer eid, Integer tid) {
        examineTopicDAO.removeByEidAndTid(eid, tid);
    }
}