package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IEsessionDAO;
import cn.gov.baiyin.court.core.entity.*;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/28.
 */
@Repository
public class EsessionDAO extends AbstractDAO implements IEsessionDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageInfo list(PageInfo pageInfo) {

        return super.queryForPageInfo(pageInfo, "select * from esession where eid=0 or eid is null", ESession.class);
    }

    @Override
    public boolean add(ESession eSession) {

        int r = super.insert(eSession);
        return r == 1;
    }

    @Override
    public void delete(int sid) {

        super.removeById(ESession.class, sid);
    }

    @Override
    public ESession findById(int sid) {
        return super.findOne(ESession.class, sid);
    }

    @Override
    public int edit(ESession eSession) {
        return super.modify(eSession);
    }

    @Override
    public List<ESession> findByEid(int examineId) {
        return super.queryList("select * from esession where eid=?", ESession.class, examineId);
    }

    @Override
    public void deleteByEid(int examineId) {

        jdbcTemplate.update("delete from esession where eid=?", examineId);
    }

    @Override
    public PageInfo listWith(PageInfo pageInfo) {
        PageInfo pi = super.queryForPageInfo(pageInfo, "select * from esession where eid>0", ESession.class);

        List<ESession> eSessions = (List<ESession>) pi.getList();

        addExamineRelation(eSessions);

        return pi;
    }

    public void addExamineRelation(List<ESession> eSessions) {
        if (!CollectionUtils.isEmpty(eSessions)) {
            StringBuilder sb = new StringBuilder();
            for (ESession eSession : eSessions) {
                if (eSession.getEid() != null) {
                    sb.append(",").append(eSession.getEid());
                }
            }
            if (sb.length() > 0) {
                sb.delete(0, 1);
            } else {
                return;
            }
            List<Examine> examines = queryList("select * from examine where id in (" + sb + ")", Examine.class);
            if (!CollectionUtils.isEmpty(examines)) {
                Map<Integer, List<Examine>> m = examines.stream().collect(Collectors.groupingBy(BaseEntity::getId));

                for (ESession eSession : eSessions) {
                    if (eSession.getEid() != null) {
                        List<Examine> es = m.get(eSession.getEid());
                        Examine e = CollectionUtils.isEmpty(es) ? null : es.get(0);
                        if (e != null) {
                            addTopicRelation(e);
                        }
                        eSession.setExamine(e);
                    }
                }
            }
        }
    }

    private void addTopicRelation(Examine e) {

        List<Topic> topics;

        List<Topic> topicList = queryList("select t2.* from examine_topic t left join examine t1 on t.eid=t1.id " +
                "left join topic t2 on t.tid=t2.id where t2.id is not null and t1.id=? order by t2.id", Topic.class, e.getId());

        //random
        if (e.getType() != null && e.getType() == 2) {

            topics = topicList.stream()
                    .collect(Collectors.groupingBy(Topic::getType))
                    .values().stream().map(this::getRandomOne)
                    .collect(Collectors.toList());
        } else {
            topics = topicList;
        }
        e.setTopics(topics);
    }

    private Topic getRandomOne(List<Topic> topics1) {
        int len = topics1.size();

        return topics1.get(RandomUtils.nextInt(len));
    }

    @Override
    public List<ESession> listUserEsessions(String userName, int waitTime) {

        String sql = "select t2.*\n" +
                "from examine_user t \n" +
                "left join user t1 on t.uid=t1.id\n" +
                "left join esession t2 on t.eid=t2.eid\n" +
                "where t1.username=? and t2.endTime > NOW()\n" +
                "and FROM_UNIXTIME(UNIX_TIMESTAMP(t2.startTime)-?) <=NOW()\n";
//                "and not EXISTS(select 1 from reply r left join examine_topic et on r.etid=et.id " +
//                "where r.uid=t1.id and et.eid=t2.eid)\n";

        List<ESession> eSessions = queryList(sql, ESession.class, userName, waitTime * 60 * 1000);
        addExamineRelation(eSessions);

        return eSessions;
    }

    @Override
    public void resetByEid(Integer id) {

        jdbcTemplate.update("update esession set eid=null where eid=?", id);
    }

    @Override
    public boolean isUserAttachEsession(String currUserName) {

        List<Boolean> list = jdbcTemplate.queryForList("select 1 from examine_user t \n" +
                "left join user t1 on t.uid=t1.id \n" +
                "where t1.username=? and t.done<>1", Boolean.class, currUserName);

        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public List<ESession> listLegelEsessions(String username, int waitTime) {
        String sql = "select * from esession t \n" +
                "where t.eid is not null and t.endTime > NOW()\n" +
                "and FROM_UNIXTIME(UNIX_TIMESTAMP(t.startTime)-?) <=NOW() " +
                "and not exists (select 1 from examine_user eu left join user u on eu.uid=u.id" +
                " where t.eid=eu.eid and u.username=? and eu.done=1)\n";
//                "and not EXISTS(select 1 from reply r left join examine_topic et on r.etid=et.id\n" +
//                "left join `user` u on r.uid=u.id where u.username=? and et.eid=t.eid)\n";
        List<ESession> eSessions = queryList(sql, ESession.class, waitTime * 60 * 1000, username);

        addExamineRelation(eSessions);
        return eSessions;
    }

    @Override
    public void resetById(Integer id) {
        jdbcTemplate.update("update esession set eid=? where id=?", null, id);
    }

    @Override
    public Boolean checkHasDone(String username, Integer eid) {
        List<ExamineUser> examineUsers = super.queryList("select t.* from examine_user t\n" +
                "left join user t1 on t.uid=t1.id\n" +
                "where t.eid=? and t1.username=?", ExamineUser.class, eid, username);

        if (CollectionUtils.isEmpty(examineUsers)) return false;
        ExamineUser examineUser = examineUsers.get(0);
        return examineUser.getDone() != null && examineUser.getDone();
    }

    @Override
    public void saveExamineUser(ExamineUser examineUser) {

        if (examineUser.getId() == null) {
            super.insert(examineUser);
        } else {
            super.modify(examineUser);
        }
    }

    @Override
    public void saveMulti(List<ESession> eSessions) {

        for (ESession eSession : eSessions) {
            insert(eSession);
        }
    }
}
