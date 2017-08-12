package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.ExamineUser;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;

/**
 * Created by WK on 2017/3/28.
 */
public interface IEsessionDAO {
    PageInfo list(PageInfo pageInfo);

    boolean add(ESession eSession);

    void delete(int sid);

    ESession findById(int sid);

    int edit(ESession eSession);

    List<ESession> findByEid(int examineId);

    void deleteByEid(int examineId);

    PageInfo listWith(PageInfo pageInfo);

    void addExamineRelation(List<ESession> eSessions);

    List<ESession> listUserEsessions(String userName, int waitTime);

    void resetByEid(Integer id);

    boolean isUserAttachEsession(String currUserName);

    List<ESession> listLegelEsessions(String username, int waitTime);

    void resetById(Integer id);

    Boolean checkHasDone(String username, Integer eid);

    void saveExamineUser(ExamineUser examineUser);

    void saveMulti(List<ESession> eSessions);

    List<ESession> findByIds(String esessionIds);
}
