package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IExamineTopicDAO;
import cn.gov.baiyin.court.core.entity.ExamineTopic;
import cn.gov.baiyin.court.core.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
@Repository
public class ExamineTopicDAO extends AbstractDAO implements IExamineTopicDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExamineTopic findOne(int eid, int topicId) {

        List<ExamineTopic> examineTopics = super.queryList("select * from examine_topic where eid=? and tid=?", ExamineTopic.class, eid, topicId);
        if (!CollectionUtils.isEmpty(examineTopics)) {
            return examineTopics.get(0);
        }
        return null;
    }

    @Override
    public boolean add(ExamineTopic examineTopic) {
        int r = super.insert(examineTopic);

        return r == 1;
    }

    @Override
    public void removeByEid(int eid) {

        jdbcTemplate.update("delete from examine_topic where eid=?", eid);
    }

    @Override
    public void removeByTid(int tid) {
        jdbcTemplate.update("delete from examine_topic where tid=?", tid);
    }

    @Override
    public List<Topic> findByEid(Integer id) {
        return super.queryList("select t1.* from examine_topic t left join topic t1 on t.tid=t1.id where t.eid=?", Topic.class, id);
    }

    @Override
    public void removeByEidAndTid(Integer eid, Integer tid) {
        jdbcTemplate.update("delete from examine_topic where eid=? and tid=?", eid, tid);
    }
}
