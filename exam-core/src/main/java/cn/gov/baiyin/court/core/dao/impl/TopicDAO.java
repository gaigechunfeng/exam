package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.dao.ITopicDAO;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
@Repository
public class TopicDAO extends AbstractDAO implements ITopicDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {
        return super.queryForPageInfo(pageInfo, "select * from topic", Topic.class);
    }

    @Override
    public boolean add(Topic topic) {

        topic.setCrtime(DateUtil.current());
        topic.setCruser(Authc.getCurrUserName());

        int r = super.insert(topic);
        return r == 1;
    }

    @Override
    public boolean deleteById(int id) {

        int r = super.removeById(Topic.class, id);
        return r == 1;
    }

    @Override
    public Topic load(int id) {

        return super.findOne(Topic.class, id);
    }

    @Override
    public boolean edit(Topic topic) {

        int r = super.modify(topic);
        return r == 1;
    }

    @Override
    public List<Topic> findByEid(Integer eid) {
        return queryList("select t2.* from examine_topic t left join topic t2 on t.tid=t2.id where t.eid=? and t2.id is not null", Topic.class, eid);
    }

//    public static void main(String[] args) {
//
//        for (Object o : System.getProperties().keySet()) {
//            System.out.println(o + "---" + System.getProperty(o.toString()));
//        }
//    }
}
