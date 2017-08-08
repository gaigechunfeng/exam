package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.dao.IEsessionDAO;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IEsessionService;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by WK on 2017/3/28.
 */
@Service
public class EsessionService implements IEsessionService {

    private IEsessionDAO esessionDAO;
    private static final int WAIT_TIME = Utils.getApp().getInt("wait.exam.time", 10);

    @Autowired
    public void setEsessionDAO(IEsessionDAO esessionDAO) {
        this.esessionDAO = esessionDAO;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {
        return esessionDAO.list(pageInfo);
    }

    @Override
    public void add(ESession eSession) throws ServiceException {

        if (StringUtils.isEmpty(eSession.getName())
                || StringUtils.isEmpty(eSession.getStartTime())
                || StringUtils.isEmpty(eSession.getEndTime())) {
            throw new ServiceException("参数错误！");
        }

        boolean r = esessionDAO.add(eSession);
        if (!r) {
            throw new ServiceException("保存记录为空！");
        }
    }

    @Override
    public void deleteById(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！");
        }
        int sid = Integer.parseInt(id);
        esessionDAO.delete(sid);
    }

    @Override
    public void applyExamine(String sid, int eid) throws ServiceException {

        ESession eSession = esessionDAO.findById(Integer.parseInt(sid));
        eSession.setEid(eid);

        int r = esessionDAO.edit(eSession);
        if (r != 1) {
            throw new ServiceException("操作记录为空！");
        }
    }

    @Override
    public List<ESession> findByEid(int examineId) {
        return esessionDAO.findByEid(examineId);
    }

    @Override
    public void removeByEid(int examineId) {
        esessionDAO.deleteByEid(examineId);
    }

    @Override
    public PageInfo listPaginationWith(PageInfo pageInfo) {
        return esessionDAO.listWith(pageInfo);
    }

    @Override
    public void addExamineRelation(List<ESession> eSessions) {
        esessionDAO.addExamineRelation(eSessions);
    }

    @Override
    public List<ESession> getCurrEsessions() {

        List<ESession> eSessions;
        String username = Authc.getCurrUserName();
        boolean isFixEsession = esessionDAO.isUserAttachEsession(username);
        if (isFixEsession && !username.startsWith("musicPlayer")) {
            eSessions = esessionDAO.listUserEsessions(username, WAIT_TIME);
        } else {
            eSessions = esessionDAO.listLegelEsessions(username, WAIT_TIME);
        }
        if (!CollectionUtils.isEmpty(eSessions)) {
            for (ESession eSession : eSessions) {
                eSession.setStartTime(DateUtil.str2long(eSession.getStartTime()));
                eSession.setEndTime(DateUtil.str2long(eSession.getEndTime()));
            }
        }
        return eSessions;
    }

    @Override
    public void resetByEid(Integer id) {

        esessionDAO.resetByEid(id);
    }

    @Override
    public void resetById(Integer id) {
        esessionDAO.resetById(id);
    }

    @Override
    public Boolean checkHasDone(Integer tid, Integer eid) {
        String username = Authc.getCurrUserName();

        return esessionDAO.checkHasDone(username, tid, eid);
    }


}
