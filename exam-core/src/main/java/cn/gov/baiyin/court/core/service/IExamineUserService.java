package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.Examine;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
public interface IExamineUserService {
    void removeByEid(int intId);

    void addByEidAndUid(int eid, int uid);

    List<ESession> findExamineByUid(int id);

    void removeByUid(int uid);

}
