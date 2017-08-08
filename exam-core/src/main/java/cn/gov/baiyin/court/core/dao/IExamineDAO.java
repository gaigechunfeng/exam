package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.ExamineInfo;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
public interface IExamineDAO {
    PageInfo listPagination(PageInfo pageInfo);

    boolean add(Examine examine);

    Examine findByName(String name);

    Examine findById(int id);

    List<Topic> findTopicsByEid(int examineId);

    void deleteById(int id);

    int edit(Examine examine);

    void saveKsxz(String ksxz, String kssm);

    ExamineInfo findKsxz();

}
