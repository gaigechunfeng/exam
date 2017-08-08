package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.Reply;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by WK on 2017/3/30.
 */
public interface IScoreDAO {
    Reply findReplyByEtidAndUid(Integer etid, Integer uid);

    boolean updateReply(Reply r);

    boolean addReply(Reply reply);

    boolean add(Score score);

    PageInfo listPagination(PageInfo pageInfo, String pos, Integer eid);

    Map<String,Object> queryScoreMap(Integer uid, Integer eid);

    List<Map<String,Object>> detail(Integer uid, Integer eid);

    List<Map<String, Object>> statistic(Integer eid);

    void removeReplyByUidAndEid(Integer uid, Integer eid);

    void removeByUidAndEid(Integer uid, Integer eid);
}
