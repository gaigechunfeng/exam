package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.entity.BaseEntity;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.SqlUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
public abstract class AbstractDAO {

    protected JdbcTemplate jdbcTemplate;

    protected <T extends BaseEntity> int insert(T t) {

        if (StringUtils.isEmpty(t.getCruser())) {
            t.setCruser(Authc.getCurrUserName());
        }
        if (StringUtils.isEmpty(t.getCrtime())) {
            t.setCrtime(DateUtil.current());
        }
        SqlUtil.SqlInfo sqlInfo = SqlUtil.genInsertSqlInfo(t);

        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getParams().toArray());
    }

    public <T extends BaseEntity> int modify(T t) {

        SqlUtil.SqlInfo sqlInfo = SqlUtil.genModifySqlInfo(t);

        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getParams().toArray());
    }

    public <T extends BaseEntity> int removeById(Class<T> cls, int id) {

        try {
            T t = cls.newInstance();
            return jdbcTemplate.update("delete from " + t.getTableName() + " where id=?", id);
        } catch (Exception e) {
            throw new RuntimeException("removeById error", e);
        }
    }

    public <T extends BaseEntity> PageInfo queryForPageInfo(PageInfo pageInfo, String sql, Class<T> cls) {
        int c = count(sql);
        List<T> list = queryList(SqlUtil.genPageSql(sql, pageInfo), cls);

        pageInfo.setList(list);
        pageInfo.setAllPage(c % pageInfo.getPageSize() == 0 ? c / pageInfo.getPageSize() : c / pageInfo.getPageSize() + 1);
        return pageInfo;
    }

    public <T extends BaseEntity> List<T> queryList(String s, Class<T> cls, Object... params) {

        return jdbcTemplate.query(s, new BeanPropertyRowMapper<T>(cls), params);
    }

    public int count(String sql) {
        String s = "select count(1) from ( " + sql + ") t ";
        return jdbcTemplate.queryForObject(s, Integer.class);
    }


    public <T extends BaseEntity> T findOne(Class<T> cls, int id) {

        try {
            String tn = cls.newInstance().getTableName();
            List<T> list = queryList("select * from " + tn + " where id=?", cls, id);
            if (!CollectionUtils.isEmpty(list)) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("findOne error", e);
        }
    }
}
