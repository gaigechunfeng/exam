package cn.gov.baiyin.court.core.service;


import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    List<Topic> listFieldsByEid(Integer eid);

    void importTopics(MultipartFile file) throws ServiceException;

    File exportAll() throws ServiceException;

    List<Topic> findByIds(String topicIds);

}
