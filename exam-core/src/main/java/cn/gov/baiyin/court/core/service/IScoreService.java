package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.Reply;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by WK on 2017/3/30.
 */
public interface IScoreService {
    Score submitAnswer(String answer, Integer speed, Float accuracy, Integer tid, Integer eid) throws ServiceException;

    Reply findReplyByEtidAndUid(Integer etid, Integer uid);

    PageInfo listPagination(PageInfo pageInfo, String pos, Integer eid);

    Map<String, Object> detail(Integer uid, Integer eid) throws ServiceException;

    Map<String, Double> statistic(Integer eid);

    Map<String, Object> frontDetail(Integer eid) throws ServiceException;

    void reExam(Integer uid, Integer eid) throws ServiceException;

}
