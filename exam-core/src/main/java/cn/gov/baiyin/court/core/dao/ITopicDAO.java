package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
public interface ITopicDAO {
    PageInfo listPagination(PageInfo pageInfo);

    boolean add(Topic topic);

    boolean deleteById(int id);

    Topic load(int id);

    boolean edit(Topic topic);

    List<Topic> findByEid(Integer eid);

    void addMulti(List<Topic> topics);

    List<Topic> findByIds(String topicIds);

    boolean existName(String name);
}
