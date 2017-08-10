package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IUserDAO;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by WK on 2017/3/25.
 */
@Repository
public class UserDAO extends AbstractDAO implements IUserDAO {

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findByUserName(String username) {
        List<User> users = jdbcTemplate.query("select * from user where username=?", new BeanPropertyRowMapper<User>(User.class), username);
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public boolean add(User user) {
        int r = super.insert(user);
        return r == 1;
    }

    @Override
    public boolean edit(User user) {
        int r = super.modify(user);
        return r == 1;
    }

    @Override
    public boolean delete(int uid) {
        int r = super.removeById(User.class, uid);
        return r == 1;
    }

    @Override
    public PageInfo listExaminee(PageInfo pageInfo) {

        String sql = "select * from user where type=1 ";

        return super.queryForPageInfo(pageInfo, sql, User.class);
    }

    @Override
    public User findById(int id) {
        return super.findOne(User.class, id);
    }

    @Override
    public void importExmainee(List<User> list) {

        for (User user : list) {
            user.setType(User.UserType.EXAMINEE.getTypeVal());
            insert(user);
        }
    }

    @Override
    public void removeReply(int uid) {
        jdbcTemplate.update("delete from reply where uid=?", uid);
    }

    @Override
    public boolean existIdcard(String idcard) {
        return jdbcTemplate.queryForObject("select count(1) from user where idcard=?", Boolean.class, idcard);
    }

}
