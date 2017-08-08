package cn.gov.baiyin.court.core.service;


import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
public interface ITopicService {
    PageInfo listPagination(PageInfo pageInfo);

    void add(Topic topic) throws ServiceException;

    void deleteById(String id) throws ServiceException;

    Topic findById(String id) throws ServiceException;

    void edit(Topic topic) throws ServiceException;

    Topic findById(Integer tid);

    List<String> listFieldsByEid(Integer eid);
}
