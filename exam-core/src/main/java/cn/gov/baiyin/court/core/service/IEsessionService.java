package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by WK on 2017/3/28.
 */
public interface IEsessionService {
    PageInfo listPagination(PageInfo pageInfo);

    void add(ESession eSession) throws ServiceException;

    void deleteById(String id) throws ServiceException;

    void applyExamine(String sid, int eid) throws ServiceException;

    List<ESession> findByEid(int examineId);

    void removeByEid(int examineId);

    PageInfo listPaginationWith(PageInfo pageInfo);

    void addExamineRelation(List<ESession> eSessions);

    List<ESession> getCurrEsessions();

    void resetByEid(Integer id);

    void resetById(Integer id);

    void checkHasDone(Integer eid) throws ServiceException;

    void examOver(Integer eid);

    void clearExamInfo(Integer eid, Integer uid);

    void importEsession(MultipartFile file) throws ServiceException;

    void delMulti(Integer[] ids) throws ServiceException;

    List<ESession> findByIds(String esessionIds);

    void edit(ESession eSession);
}
