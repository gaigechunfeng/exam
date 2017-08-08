package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.ExamineInfo;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.Map;

/**
 * Created by WK on 2017/3/26.
 */
public interface IExamineService {
    PageInfo listPagination(PageInfo pageInfo);

    void add(Examine examine, String ids, String topicIds) throws ServiceException;

    Map<String,Object> detail(String id) throws ServiceException;

    void deleteById(String id) throws ServiceException;

    void edit(Examine examine, String topicIds, String esessionIds) throws ServiceException;

    void changeKsxz(String ksxz, String kssm) throws ServiceException;

    ExamineInfo findKsxz();
}
