package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IExamineUserDAO;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.ExamineUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
@Repository
public class ExamineUserDAO extends AbstractDAO implements IExamineUserDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeByEid(int eid) {

        jdbcTemplate.update("delete from examine_user where eid in (select id from esession where eid=?)", eid);
    }

    @Override
    public ExamineUser find(int eid, int uid) {

        List<ExamineUser> examineUsers = super.queryList("select * from examine_user where eid=? and uid=?", ExamineUser.class, eid, uid);
        if (!CollectionUtils.isEmpty(examineUsers)) {
            return examineUsers.get(0);
        }
        return null;
    }

    @Override
    public void add(int eid, int uid) {

        super.insert(new ExamineUser(eid, uid));
    }

    @Override
    public List<ESession> findExamineByUid(int uid) {

        return super.queryList("select t1.* from examine_user t left join esession t1 on t.eid=t1.id where t1.id is not null and t.uid=? ", ESession.class, uid);
    }

    @Override
    public void removeByUid(int uid) {

        jdbcTemplate.update("delete from examine_user where uid=?", uid);
    }
}
