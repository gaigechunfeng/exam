package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.ExamineTopic;
import cn.gov.baiyin.court.core.entity.Topic;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
public interface IExamineTopicDAO {
    ExamineTopic findOne(int eid, int topicId);

    boolean add(ExamineTopic examineTopic);

    void removeByEid(int eid);

    void removeByTid(int tid);

    List<Topic> findByEid(Integer id);

    void removeByEidAndTid(Integer eid, Integer tid);
}
