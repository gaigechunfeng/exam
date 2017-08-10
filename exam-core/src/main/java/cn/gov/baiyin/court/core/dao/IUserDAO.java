package cn.gov.baiyin.court.core.dao;

import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.util.PageInfo;

import java.util.List;

/**
 * Created by WK on 2017/3/25.
 */
public interface IUserDAO {
    User findByUserName(String username);

    boolean add(User user);

    boolean edit(User user);

    boolean delete(int uid);

    PageInfo listExaminee(PageInfo pageInfo);

    User findById(int id);

    void importExmainee(List<User> list);

    void removeReply(int uid);

    boolean existIdcard(String idcard);
}
