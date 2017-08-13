package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.IUserDAO;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.*;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.HashUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WK on 2017/3/25.
 */
@Service
public class UserService implements IUserService {

    private IUserDAO userDAO;
    private IExamineUserService examineUserService;
    private IEsessionService esessionService;
    private IFileService fileService;

    @Autowired
    public void setFileService(IFileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setEsessionService(IEsessionService esessionService) {
        this.esessionService = esessionService;
    }

    @Autowired
    public void setExamineUserService(IExamineUserService examineUserService) {
        this.examineUserService = examineUserService;
    }

    @Autowired
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User findByUserName(String username) {
        return userDAO.findByUserName(username);
    }

    @Override
    public PageInfo list(PageInfo pageInfo) {
        return null;
    }

    @Override
    @Transactional
    public void add(User user, String examineIds) throws ServiceException {
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getUsername())) {
            throw new ServiceException("考生姓名和准考证号不能为空！");
        }
        user.setType(User.UserType.EXAMINEE.getTypeVal());
        user.setPermission(10);
        if (!StringUtils.isEmpty(user.getPhoto())) {
            try {
                moveAndSet(user);
            } catch (IOException e) {
                throw new ServiceException("move user photo error!" + e.getMessage(), e);
            }
        }
        User persist = userDAO.findByUserName(user.getUsername());
        if (persist != null) {
            throw new ServiceException("准考证号是[" + user.getUsername() + "]的考试已经存在！");
        }

        boolean r = userDAO.add(user);
        if (!r) {
            throw new ServiceException("操作结果为空！");
        }

        persist = userDAO.findByUserName(user.getUsername());

        if (!StringUtils.isEmpty(examineIds)) {
            String[] ids = examineIds.split(",");
            for (String id : ids) {
                examineUserService.addByEidAndUid(Integer.parseInt(id), persist.getId());
            }

        }
    }

    private static void moveAndSet(User user) throws IOException {
        File productionFile = FileUtil.moveTemp2Pro(user.getPhoto());
        user.setPhoto(productionFile.getAbsolutePath());
    }

    @Override
    @Transactional
    public void edit(User user, String examineIds) throws ServiceException {

        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPos())) {
            throw new ServiceException("姓名、准考证号和岗位代码不能为空！");
        }
        User persist = userDAO.findById(user.getId());

        if (persist == null) throw new ServiceException("未找到用户对象！[id=" + user.getId() + "]");
        if (!StringUtils.isEmpty(persist.getPhoto()) && !persist.getPhoto().equals(user.getPhoto())) {
            try {
                FileUtil.deleteFile(persist.getPhoto());
            } catch (IOException e) {
                throw new ServiceException("delete photo error", e);
            }
        }
        if (!StringUtils.isEmpty(user.getPhoto()) && FileService.isTempFile(user.getPhoto())) {
            try {
                moveAndSet(user);
            } catch (IOException e) {
                throw new ServiceException("move photo error!", e);
            }
        }
        examineUserService.removeByUid(user.getId());
        if (!StringUtils.isEmpty(examineIds)) {
            String[] ids = examineIds.split(",");
            for (String id : ids) {
                examineUserService.addByEidAndUid(Integer.parseInt(id), user.getId());
            }
        }
        BeanUtils.copyProperties(persist, user, "name", "username", "photo", "sex", "idcard", "pos");

        boolean r = userDAO.edit(user);
        if (!r) {
            throw new ServiceException("操作记录为空！");
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) throws ServiceException {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！");
        }

        int uid = Integer.parseInt(id);
        userDAO.delete(uid);
        examineUserService.removeByUid(uid);
        userDAO.removeReply(uid);
    }

    @Override
    public PageInfo listExaminee(PageInfo pageInfo) {

        return userDAO.listExaminee(pageInfo);
    }

    @Override
    public User login(String username, String password) throws ServiceException {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ServiceException("用户名或者密码不能为空！");
        }
        User user = userDAO.findByUserName(username);
        if (user == null) {
            throw new ServiceException("没有该用户![" + username + "]");
        }
        if (!HashUtil.md5(password).equals(user.getPassword())) {
            throw new ServiceException("密码错误！");
        }

        return user;
    }

    @Override
    public void changePwd(User user, String oldPwd, String newPwd, String newPwda) throws ServiceException {

        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(newPwda)) {
            throw new ServiceException("原密码、新密码、重复新密码不能为空！");
        }
        if (!newPwd.equals(newPwda)) {
            throw new ServiceException("重复新密码不一致！");
        }
        if (!HashUtil.md5(oldPwd).equals(user.getPassword())) {
            throw new ServiceException("原密码输入错误！");
        }
        user.setPassword(HashUtil.md5(newPwd));
        boolean r = userDAO.edit(user);

        if (!r) {
            throw new ServiceException("保存失败！");
        }
    }

    @Override
    public Map<String, Object> findById(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！");
        }
        User user = userDAO.findById(Integer.parseInt(id));
        if (user == null) {
            throw new ServiceException("未找到用户对象！[id=" + id + "]");
        }
        List<ESession> eSessions = examineUserService.findExamineByUid(user.getId());
        esessionService.addExamineRelation(eSessions);

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("esessions", eSessions);

        return data;
    }

    @Override
    public User frontLogin(String name, String username, String idcard) throws ServiceException {

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(username)) {
            throw new ServiceException("\u59d3\u540d\uff0c\u51c6\u8003\u8bc1\u53f7\u548c\u8eab\u4efd\u8bc1\u53f7\u4e0d\u80fd\u4e3a\u7a7a\uff01");
        }
        User user = userDAO.findByUserName(username);
        if (user == null) {
            throw new ServiceException("\u51c6\u8003\u8bc1\u53f7[" + username + "]\u7684\u8003\u751f\u4e0d\u5b58\u5728\uff01");
        }
        if (!name.equals(user.getName())) {
            throw new ServiceException("\u8003\u751f\u59d3\u540d\u9519\u8bef\uff01");
        }
        if (!username.startsWith("musicPlayer")) {
            if (!idcard.equals(user.getIdcard())) {
                throw new ServiceException("\u8003\u751f\u8eab\u4efd\u8bc1\u53f7\u7801\u9519\u8bef\uff01");
            }
        }

        return user;
    }

    @Override
    @Transactional
    public String importUser(MultipartFile file) throws ServiceException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("file not exist");
        }
        String fileName = file.getOriginalFilename();
        if (!FileUtil.isXls(fileName)) {
            throw new RuntimeException("\u8bf7\u4e0a\u4f20\u7b26\u5408\u683c\u5f0f\u7684excel\u6587\u4ef6\uff01");
        }

        String filePath = fileService.save(file);
        File tmpFile = new File(FileService.getTempFolder(), filePath);
        List<User> list = new ArrayList<>();
        try {
            Workbook wb = Workbook.getWorkbook(tmpFile);
            Sheet sheet = wb.getSheet(0);
//            int cols = sheet.getColumns();
            int rows = sheet.getRows();

            for (int i = 1; i < rows; i++) {

                String name = sheet.getCell(0, i).getContents();
                if (StringUtils.isEmpty(name)) continue;
                String username = sheet.getCell(1, i).getContents();
                String sex = sheet.getCell(2, i).getContents();
                String idcard = sheet.getCell(3, i).getContents();
                String pos = sheet.getCell(4, i).getContents();

                if (StringUtils.isEmpty(name) || StringUtils.isEmpty(username) || StringUtils.isEmpty(idcard)
                        || StringUtils.isEmpty(pos)) {
                    throw new RuntimeException("\u59d3\u540d\uff0c\u51c6\u8003\u8bc1\u53f7\uff0c\u8eab\u4efd\u8bc1\u53f7\uff0c\u5c97\u4f4d\u4ee3\u7801\u4e0d\u80fd\u4e3a\u7a7a\uff01");
                }
                if (existIdcard(idcard)) {
                    //相同的身份证号码的考试已经存在！
                    throw new RuntimeException("\u76f8\u540c\u7684\u8eab\u4efd\u8bc1\u53f7\u7801\u7684\u8003\u751f\u5df2\u7ecf\u5b58\u5728\uff01");
                }

                User user = new User();
                user.setName(name);
                user.setUsername(username);
                user.setSex("男".equals(sex) ? new Integer(1) : (!"女".equals(sex) ? null : 2));
                user.setIdcard(idcard);
                user.setPos(pos);
                list.add(user);
            }

            userDAO.importExmainee(list);
        } catch (IOException | BiffException e) {
            throw new RuntimeException("\u5bfc\u5165\u8003\u751f\u5931\u8d25\uff01" + e.getMessage(), e);
        } finally {
            FileUtil.deleteFile(tmpFile);
        }

        return filePath;
    }

    private boolean existIdcard(String idcard) {
        return userDAO.existIdcard(idcard);
    }

    @Override
    @Transactional
    public void deleteMulti(Integer[] ids) throws ServiceException {

        for (Integer id : ids) {
            deleteById(id + "");
        }
    }
}
