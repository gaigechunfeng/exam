package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.IExamineUserDAO;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.ExamineUser;
import cn.gov.baiyin.court.core.service.IExamineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by WK on 2017/3/27.
 */
@Service
public class ExamineUserService implements IExamineUserService {

    private IExamineUserDAO examineUserDAO;

    @Autowired
    public void setExamineUserDAO(IExamineUserDAO examineUserDAO) {
        this.examineUserDAO = examineUserDAO;
    }

    @Override
    public void removeByEid(int eid) {
        examineUserDAO.removeByEid(eid);
    }

    @Override
    public void addByEidAndUid(int eid, int uid) {

        if (findByEidAndUid(eid, uid) == null) {

            examineUserDAO.add(eid, uid);
        }
    }

    @Override
    public List<ESession> findExamineByUid(int uid) {
        return examineUserDAO.findExamineByUid(uid);
    }

    @Override
    public void removeByUid(int uid) {

        examineUserDAO.removeByUid(uid);
    }

    private ExamineUser findByEidAndUid(int eid, int uid) {
        return examineUserDAO.find(eid, uid);
    }
}
