package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.ExamineTopic;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
public interface IExamineTopicService {
    ExamineTopic addByEidAndTid(int eid, int topicId) throws ServiceException;

    void removeByEid(int eid);

    void removeByTid(int tid);

    ExamineTopic findByEidAndTid(int eid, int topicId);

    /**
     * 根据考试的id查询改考试的所有题目
     *
     * @param id 考试id
     * @return
     */
    List<Topic> findByEid(Integer id);

    void removeRelationByEidAndTid(Integer eid, Integer tid);
}
