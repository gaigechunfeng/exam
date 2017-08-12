package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.Reply;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.io.File;
import java.util.Map;

/**
 * Created by WK on 2017/3/30.
 */
public interface IScoreService {
    Score submitAnswer(String answer, Integer speed, Float accuracy, Integer tid, Integer eid, Integer esId) throws ServiceException;

    Reply findReplyByEtidAndUid(Integer etid, Integer uid);

    PageInfo listPagination(PageInfo pageInfo, String pos, Integer eid);

    Map<String, Object> detail(Integer uid, Integer eid) throws ServiceException;

    Map<String, Double> statistic(Integer eid, String pos);

    Map<String, Object> frontDetail(Integer eid) throws ServiceException;

    void reExam(Integer uid, Integer eid) throws ServiceException;

    String exportDb2Sql();

    File genDataUploadZip(Integer eid, String pos) throws ServiceException;
}
