package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IScoreDAO;
import cn.gov.baiyin.court.core.entity.Reply;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WK on 2017/3/30.
 */
@Repository
public class ScoreDAO extends AbstractDAO implements IScoreDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reply findReplyByEtidAndUid(Integer etid, Integer uid) {

        List<Reply> replies = queryList("select * from reply where etid=? and uid=?", Reply.class, etid, uid);
        if (!CollectionUtils.isEmpty(replies)) {
            return replies.get(0);
        }
        return null;
    }

    @Override
    public boolean updateReply(Reply r) {

        return super.modify(r) == 1;
    }

    @Override
    public boolean addReply(Reply reply) {
        return super.insert(reply) == 1;
    }

    @Override
    public boolean add(Score score) {

        return insert(score) == 1;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo, String pos, Integer eid) {

        String s = StringUtils.isEmpty(pos) ? "" : " and t2.pos='" + pos + "'";
        return super.queryForPageInfo(pageInfo, "select DISTINCT t2.* from reply t\n" +
                "left join examine_topic t1 on t.etid=t1.id\n" +
                "left join `user` t2 on t.uid=t2.id\n" +
                "where t2.id is not null " + s + " and t1.eid=" + eid, User.class);
    }

    @Override
    public Map<String, Object> queryScoreMap(Integer uid, Integer eid) {

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select t2.name,t3.score from reply t\n" +
                "left join examine_topic t1 on t.etid=t1.id\n" +
                "left join topic t2 on t1.tid=t2.id\n" +
                "left join score t3 on t3.rid=t.id\n" +
                "where t.uid=? and t1.eid=?", uid, eid);
        Map<String, Object> m = new HashMap<>();

        for (Map<String, Object> map : list) {
            m.put(map.get("name").toString(), map.get("score"));
        }
        return m;
    }

    @Override
    public List<Map<String, Object>> detail(Integer uid, Integer eid) {
        return jdbcTemplate.queryForList("select t2.id uid,t4.id eid,t2.name,t2.username,t5.startTime,t2.photo,t7.`name` topicname,t.score " +
                "from score t\n" +
                "left join reply t1 on t.rid=t1.id\n" +
                "left join user t2 on t1.uid=t2.id\n" +
                "left join examine_topic t3 on t1.etid=t3.id\n" +
                "left join topic t7 on t3.tid=t7.id\n" +
                "left join examine t4 on t3.eid=t4.id\n" +
                "left join esession t5 on t5.eid=t4.id\n" +
                "where t2.id=? and t4.id=?", uid, eid);
    }

    @Override
    public List<Map<String, Object>> statistic(Integer eid) {

        return jdbcTemplate.queryForList("select t3.name,avg(t.score) score  from score t\n" +
                "left join reply t1 on t.rid=t1.id\n" +
                "left join examine_topic t2 on t1.etid=t2.id\n" +
                "left join topic t3 on t2.tid= t3.id\n" +
                "left join user t4 on t1.uid= t4.id\n" +
                "where t4.id is not null and t2.eid=? group by t3.name", eid);
    }

    @Override
    public void removeReplyByUidAndEid(Integer uid, Integer eid) {

        jdbcTemplate.update("delete t.* from reply t left join examine_topic t1 on t.etid=t1.id where t.uid=? and t1.eid=?", uid, eid);
    }

    @Override
    public void removeByUidAndEid(Integer uid, Integer eid) {

        jdbcTemplate.update("delete t.* from score t left join reply t1 on t.rid=t1.id left join examine_topic t2 on t1.etid=t2.id " +
                "where t1.uid=? and t2.eid=?", uid, eid);
    }
}
