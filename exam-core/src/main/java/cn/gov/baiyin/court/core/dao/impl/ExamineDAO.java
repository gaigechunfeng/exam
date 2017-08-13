package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.dao.IExamineDAO;
import cn.gov.baiyin.court.core.entity.*;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/26.
 */
@Repository
public class ExamineDAO extends AbstractDAO implements IExamineDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {
        String sql = "select * from examine ";

        PageInfo pi = super.queryForPageInfo(pageInfo, sql, Examine.class);
        List<Examine> examines = (List<Examine>) pi.getList();

        if (!CollectionUtils.isEmpty(examines)) {
            StringBuilder ids = new StringBuilder();
            for (Examine examine : examines) {
                ids.append(",").append(examine.getId());
            }
            if (ids.length() > 0) {
                ids.delete(0, 1);
            }
            List<ESession> eSessions = queryList("select * from esession where eid in (" + ids + ")", ESession.class);

            if (!CollectionUtils.isEmpty(eSessions)) {

                Map<Integer, List<ESession>> m = eSessions.stream().collect(Collectors.groupingBy(ESession::getEid));
                for (Examine examine : examines) {
                    examine.seteSessions(m.get(examine.getId()));
                }
            }
        }
        return pi;

    }

    @Override
    public boolean add(Examine examine) {

        examine.setCruser(Authc.getCurrUserName());
        examine.setCrtime(DateUtil.current());
        int r = super.insert(examine);
        return r == 1;
    }

    @Override
    public Examine findByName(String name) {

        List<Examine> examines = super.queryList("select * from examine where name=?", Examine.class, name);
        if (!CollectionUtils.isEmpty(examines)) {
            return examines.get(0);
        }
        return null;
    }

    @Override
    public Examine findById(int id) {

        return super.findOne(Examine.class, id);
    }

    @Override
    public List<Topic> findTopicsByEid(int examineId) {

        return super.queryList("select t1.* from examine_topic t LEFT JOIN topic t1 on t.tid=t1.id where eid=? ", Topic.class, examineId);
    }

    @Override
    public void deleteById(int id) {

        super.removeById(Examine.class, id);
    }

    @Override
    public int edit(Examine examine) {

        return super.modify(examine);
    }

    @Override
    public void saveKsxz(String ksxz, String kssm) {

        int r = jdbcTemplate.update("update examine_info set ksxz=?,kssm=? ", ksxz, kssm);
        if (r == 0) {
            r = jdbcTemplate.update("insert into examine_info(ksxz,kssm) values(?,?)", ksxz, kssm);
            if (r != 1) {
                throw new RuntimeException("save error");
            }
        }
    }

    @Override
    public ExamineInfo findKsxz() {

        List<ExamineInfo> examineInfos = queryList("select * from examine_info", ExamineInfo.class);
        return CollectionUtils.isEmpty(examineInfos) ? null : examineInfos.get(0);
    }

    @Override
    public ExamineUser findEUByEidAndUname(Integer eid, String username) {

        String s = "select t.* from examine_user t left join user t1 on t.uid=t1.id where t.eid=? and t1.username=?";
        List<ExamineUser> examineUsers = super.queryList(s, ExamineUser.class, eid, username);
        if (!CollectionUtils.isEmpty(examineUsers)) {
            return examineUsers.get(0);
        }
        return null;
    }

    @Override
    public ExamineUser findEUByEidAndUid(Integer eid, Integer uid) {
        List<ExamineUser> examineUsers = super.queryList("select * from examine_user where eid=? and uid=?", ExamineUser.class, eid, uid);
        if (!CollectionUtils.isEmpty(examineUsers)) {
            return examineUsers.get(0);
        }
        return null;
    }

}
