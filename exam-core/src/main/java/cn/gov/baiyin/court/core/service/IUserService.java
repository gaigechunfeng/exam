package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by WK on 2017/3/25.
 */
public interface IUserService {
    User findByUserName(String username);

    PageInfo list(PageInfo pageInfo);

    void add(User user, String examineIds) throws ServiceException;

    void edit(User user, String examineIds) throws ServiceException;

    void deleteById(String uid) throws ServiceException;

    PageInfo listExaminee(PageInfo pageInfo);

    User login(String username, String password) throws ServiceException;

    void changePwd(User o, String oldPwd, String newPwd, String newPwda) throws ServiceException;

    Map<String, Object> findById(String id) throws ServiceException;

    /**
     * 前台登录
     *
     * @param name     考生姓名
     * @param username 考生准考证号
     * @param idcard   考生身份证号码
     * @return
     * @throws ServiceException
     */
    User frontLogin(String name, String username, String idcard) throws ServiceException;

    String importUser(MultipartFile file) throws ServiceException;

    /**
     * 批量删除用户
     *
     * @param ids
     */
    void deleteMulti(Integer[] ids) throws ServiceException;
}
