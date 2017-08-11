package cn.gov.baiyin.court.core.dao.impl;

import cn.gov.baiyin.court.core.dao.IScoreDAO;
import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.Reply;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.service.IExamineService;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/30.
 */
@Repository
public class ScoreDAO extends AbstractDAO implements IScoreDAO {

    private IExamineService examineService;

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

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
    public Map<String, Object> queryScoreMap(Integer eid) {

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select t2.id,t.uid,max(t3.score) score from reply t\n" +
                "left join examine_topic t1 on t.etid=t1.id\n" +
                "left join topic t2 on t1.tid=t2.id\n" +
                "left join score t3 on t3.rid=t.id\n" +
                "where t1.eid=? group by t2.id,t.uid", eid);
        Map<String, Object> m = new HashMap<>();

        for (Map<String, Object> map : list) {
            m.put(MapUtils.getString(map, "id", "") + "-" + MapUtils.getString(map, "uid", ""),
                    MapUtils.getFloat(map, "score", 0F));
        }
        return m;
    }

    @Override
    public List<Map<String, Object>> detail(Integer uid, Integer eid) {

        Examine examine = examineService.findById(eid);
        if (examine != null && examine.getType() == 2) {
            return jdbcTemplate.queryForList("select t2.id uid,t4.id eid,t2.name,t2.username,t5.startTime,t2.photo," +
                    "case when t7.type=1 then '对照复录' else '听音打字' end topicname,max(t.score) score " +
                    "from score t\n" +
                    "left join reply t1 on t.rid=t1.id\n" +
                    "left join user t2 on t1.uid=t2.id\n" +
                    "left join examine_topic t3 on t1.etid=t3.id\n" +
                    "left join topic t7 on t3.tid=t7.id\n" +
                    "left join examine t4 on t3.eid=t4.id\n" +
                    "left join esession t5 on t5.eid=t4.id\n" +
                    "where t2.id=? and t4.id=? " +
                    "group by t2.id,t4.id,t2.name,t2.username,t5.startTime,t2.photo,t7.type order by t7.type", uid, eid);
        } else {
            return jdbcTemplate.queryForList("select t2.id uid,t4.id eid,t2.name,t2.username,t5.startTime,t2.photo,t7.`name` topicname,t.score " +
                    "from score t\n" +
                    "left join reply t1 on t.rid=t1.id\n" +
                    "left join user t2 on t1.uid=t2.id\n" +
                    "left join examine_topic t3 on t1.etid=t3.id\n" +
                    "left join topic t7 on t3.tid=t7.id\n" +
                    "left join examine t4 on t3.eid=t4.id\n" +
                    "left join esession t5 on t5.eid=t4.id\n" +
                    "where t2.id=? and t4.id=? order by t7.id", uid, eid);
        }

    }

    @Override
    public Map<String, Double> statistic(Integer eid, String pos) {

        Examine examine = examineService.findById(eid);
        List<Map<String, Object>> list;
        if (examine.getType() == 2) {

            String s = "select t4.type,sum(t.score) score from score t\n" +
                    "left join reply t1 on t.rid=t1.id\n" +
                    "left join examine_topic t2 on t1.etid=t2.id\n" +
                    "left join user t3 on t1.uid=t3.id\n" +
                    "left join topic t4 on t2.tid=t4.id\n" +
                    "where eid=?  " + (StringUtils.isEmpty(pos) ? "" : " and t3.pos='" + pos + " '") + "group by t4.type order by t4.type";
            list = jdbcTemplate.queryForList(s, eid);
            if (!CollectionUtils.isEmpty(list)) {
                for (Map<String, Object> map : list) {
                    Integer type = MapUtils.getInteger(map, "type", 0);
                    map.put("name", type == 1 ? "对照复录" : "听音打字");
                }
            }
        } else {
            list = jdbcTemplate.queryForList("select t3.name,t.score  from score t\n" +
                    "left join reply t1 on t.rid=t1.id\n" +
                    "left join examine_topic t2 on t1.etid=t2.id\n" +
                    "left join topic t3 on t2.tid= t3.id\n" +
                    "left join user t4 on t1.uid= t4.id\n" +
                    "where t4.id is not null " + (StringUtils.isEmpty(pos) ? "" : " and t4.pos='" + pos + "'") +
                    " and t2.eid=? order by t3.id", eid);
        }

        return list.stream()
                .collect(Collectors.groupingBy(m -> m.get("name").toString(),
                        Collectors.averagingDouble(m -> MapUtils.getFloatValue(m, "score", 0F))));
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

    @Override
    public String exportDb2Sql() {

        String queryTablenameSql = "select table_name from information_schema.tables where TABLE_SCHEMA=?";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(queryTablenameSql, "exam");
        if (!CollectionUtils.isEmpty(list)) {

            return list.stream()
                    .map(m -> {
                        String tableName = MapUtils.getString(m, "table_name", "");
                        if (!StringUtils.isEmpty(tableName)) {
                            List<Map<String, Object>> subList = jdbcTemplate.queryForList("select * from " + tableName);
                            if (!CollectionUtils.isEmpty(subList)) {
                                return subList.stream()
                                        .map(sm -> map2Sql(tableName, sm)).reduce((s1, s2) -> s1 + "\r\n" + s2).orElse("");
                            }
                        }
                        return "";
                    }).reduce((s1, s2) -> s1 + "\r\n" + s2).orElse("");
        }
        return "";
    }

    @Override
    public void removeByEtidAndUid(Integer etid, Integer uid) {

        jdbcTemplate.update("delete from reply where etid=? and uid=?", etid, uid);
    }

    @Override
    public PageInfo listRandom(PageInfo pageInfo, String pos, Integer eid) {

//        String s = "select t2.id,t2.name,t2.username,t2.pos,t4.type,t.score\n" +
//                "from score t\n" +
//                "left join reply t1 on t.rid=t1.id\n" +
//                "left join user t2 on t1.uid=t2.id\n" +
//                "left join examine_topic t3 on t1.etid=t3.id\n" +
//                "left join topic t4 on t3.tid=t4.id\n" +
//                "where t3.eid=?";
        String s = "select t2.id,t2.name,t2.username,t2.pos,\n" +
                "sum(case when t4.type=1 then t.score else 0 end) type1,\n" +
                "sum(case when t4.type=2 then t.score else 0 end) type2\n" +
                "from score t\n" +
                "left join reply t1 on t.rid=t1.id\n" +
                "left join user t2 on t1.uid=t2.id\n" +
                "left join examine_topic t3 on t1.etid=t3.id\n" +
                "left join topic t4 on t3.tid=t4.id\n" +
                "where t3.eid=?\n" +
                "group by t2.id,t2.name,t2.username,t2.pos";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(s, eid);

        if (!CollectionUtils.isEmpty(list)) {

            for (Map<String, Object> map : list) {
                map.put("scoreFields", new String[]{"\u5bf9\u7167\u9644\u5f55", "\u542c\u97f3\u6253\u5b57", "\u603b\u5206"});
            }
        }

        return null;
    }

    @Override
    public Map<String, Object> queryRandomScoreMap(Integer eid) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select t2.type id,t.uid,max(t3.score) score from reply t\n" +
                "left join examine_topic t1 on t.etid=t1.id\n" +
                "left join topic t2 on t1.tid=t2.id\n" +
                "left join score t3 on t3.rid=t.id\n" +
                "where t1.eid=? group by t2.type,t.uid order by id", eid);
        Map<String, Object> m = new HashMap<>();

        for (Map<String, Object> map : list) {
            m.put(MapUtils.getString(map, "id", "") + "-" + MapUtils.getString(map, "uid", ""),
                    MapUtils.getFloat(map, "score", 0F));
        }
        return m;
    }

    private static String map2Sql(String tableName, Map<String, Object> sm) {

        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        StringBuilder r = new StringBuilder("insert into " + tableName + "(");

        for (Map.Entry<String, Object> entry : sm.entrySet()) {
            String field = entry.getKey();
            Object val = entry.getValue();

            if ("id".equalsIgnoreCase(field)) {
                continue;
            }
            fields.append(",").append(field);
            values.append(",").append(parseVal(val));
        }
        if (fields.length() > 0) {
            fields.delete(0, 1);
        }
        if (values.length() > 0) {
            values.delete(0, 1);
        }
        r.append(fields).append(") values(").append(values).append(");");

        return r.toString();
    }

    private static String parseVal(Object val) {
        if (val == null) return null;
        Class cls = val.getClass();

        if (cls.isPrimitive() || Number.class.isAssignableFrom(cls)) {
            return String.valueOf(val);
        } else {
            return "'" + val + "'";
        }
    }
}
