package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.dao.IEsessionDAO;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.ExamineUser;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.entity.User;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IEsessionService;
import cn.gov.baiyin.court.core.service.IExamineService;
import cn.gov.baiyin.court.core.service.IUserService;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.Utils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/28.
 */
@Service
public class EsessionService implements IEsessionService {

//    private static final Logger LOG = LoggerFactory.getLogger(EsessionService.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private IUserService userService;
    private IEsessionDAO esessionDAO;
    private IExamineService examineService;
    private static final int WAIT_TIME = Utils.getApp().getInt("wait.exam.time", 10);

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

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

        boolean isMusic = username.startsWith("musicPlayer");
        boolean isFixEsession = esessionDAO.isUserAttachEsession(username);

        if (isFixEsession && !isMusic) {
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
        return eSessions.stream()
                .filter(eSession -> !esessionDAO.checkHasDone(username, eSession.getEid()))
                .collect(Collectors.toList());
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
    public void checkHasDone(Integer eid) throws ServiceException {
        String username = Authc.getCurrUserName();

        boolean r = esessionDAO.checkHasDone(username, eid);
        if (r) {
            throw new ServiceException("\u60a8\u5df2\u7ecf\u53c2\u52a0\u8fc7\u672c\u6b21\u8003\u8bd5\uff0c\u4e0d\u80fd\u91cd\u590d\u53c2\u52a0\uff01");
        }
    }

    @Override
    public void examOver(Integer eid) {

        String username = Authc.getCurrUserName();

        User user = userService.findByUserName(username);
        ExamineUser examineUser = examineService.findEUByEidAndUname(eid, username);
        if (examineUser == null) {
            examineUser = new ExamineUser(eid, user.getId());
        }
        examineUser.setDone(true);
        esessionDAO.saveExamineUser(examineUser);
    }

    @Override
    public void clearExamInfo(Integer eid, Integer uid) {

        ExamineUser examineUser = examineService.findEUByEidAndUid(eid, uid);
        if (examineUser != null) {
            examineUser.setDone(false);
            esessionDAO.saveExamineUser(examineUser);
        }
    }

    @Override
    @Transactional
    public void importEsession(MultipartFile file) throws ServiceException {

        String fileName = file.getOriginalFilename();
        if (!FileUtil.isXls(fileName)) {
            throw new ServiceException("\u5bfc\u5165\u6587\u4ef6\u5fc5\u987b\u662fxls\u6587\u4ef6\uff01");
        }
        File tmpFile = saveFile(file);

        try {
            Workbook wb = Workbook.getWorkbook(tmpFile);
            Sheet sheet = wb.getSheet(0);

            int rowCount = sheet.getRows();
            List<ESession> eSessions = new ArrayList<>(rowCount - 1);
            for (int i = 1; i < rowCount; i++) {

                String name = sheet.getCell(0, i).getContents();
                String startTime = sheet.getCell(1, i).getContents();
                String endTime = sheet.getCell(2, i).getContents();

                checkEsessionTime(startTime);
                checkEsessionTime(endTime);

                ESession eSession = new ESession();
                eSession.setName(name);
                eSession.setStartTime(startTime);
                eSession.setEndTime(endTime);
                eSessions.add(eSession);
            }

            esessionDAO.saveMulti(eSessions);
        } catch (IOException | BiffException e) {
            throw new ServiceException("\u5bfc\u5165\u573a\u6b21\u5931\u8d25\uff01", e);
        } finally {
            FileUtil.deleteFile(tmpFile);
        }
    }

    @Override
    @Transactional
    public void delMulti(Integer[] ids) throws ServiceException {

        for (Integer id : ids) {
            deleteById(String.valueOf(id));
        }
    }

    @Override
    public List<ESession> findByIds(String esessionIds) {
        return esessionDAO.findByIds(esessionIds);
    }

    @Override
    public void edit(ESession eSession) {
        esessionDAO.edit(eSession);
    }

    private static void checkEsessionTime(String startTime) {
        LocalDateTime.parse(startTime, FORMATTER);
    }

    private static File saveFile(MultipartFile file) throws ServiceException {

        String folder = System.getProperty("java.io.tmpdir") + "\\exam";
        File destFile = new File(folder, "esession-" + System.currentTimeMillis() + ".xls");

        FileUtil.createFile(destFile);
        try (InputStream is = file.getInputStream(); FileOutputStream fos = new FileOutputStream(destFile)) {
            StreamUtils.copy(is, fos);
        } catch (IOException e) {
            throw new ServiceException("\u4fdd\u5b58xls\u6587\u4ef6\u5931\u8d25\uff01", e);
        }
        return destFile;
    }
}
