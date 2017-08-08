package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.ExamineUser;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
public interface IExamineUserDAO {
    void removeByEid(int eid);

    ExamineUser find(int eid, int uid);

    void add(int eid, int uid);

    List<ESession> findExamineByUid(int uid);

    void removeByUid(int uid);
}
