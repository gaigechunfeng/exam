package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.ITopicDAO;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineTopicService;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.Utils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/26.
 */
@Service
public class TopicService implements ITopicService {

    private ITopicDAO topicDAO;
    private IExamineTopicService examineTopicService;

    @Autowired
    public void setExamineTopicService(IExamineTopicService examineTopicService) {
        this.examineTopicService = examineTopicService;
    }

    @Autowired
    public void setTopicDAO(ITopicDAO topicDAO) {
        this.topicDAO = topicDAO;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {
        return topicDAO.listPagination(pageInfo);
    }

    @Override
    public void add(Topic topic) throws ServiceException {

        if (StringUtils.isEmpty(topic.getName()) || StringUtils.isEmpty(topic.getContent())) {
            throw new ServiceException("试题名称和内容不能为空！");
        }

        if (topic.getType() == Topic.Type.LISTEN.getTypeVal()) {
            //将临时文件夹中的文件拷贝到正式资料文件夹
            try {
                moveAndSet(topic);
            } catch (java.io.IOException e) {
                throw new ServiceException("copy file from temp dir to production dir error", e);
            }
        }

        boolean r = topicDAO.add(topic);
        if (!r) {
            throw new ServiceException("保存试题失败！");
        }
    }

    @Override
    public void deleteById(String id) throws ServiceException {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误[id]");
        }

        int tid = Integer.parseInt(id);
        boolean r = topicDAO.deleteById(tid);
        if (!r) {
            throw new ServiceException("操作失败！");
        }

        examineTopicService.removeByTid(tid);
    }

    @Override
    public Topic findById(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！[id]");
        }

        return topicDAO.load(Integer.parseInt(id));
    }

    @Override
    public void edit(Topic topic) throws ServiceException {
        if (StringUtils.isEmpty(topic.getName()) || StringUtils.isEmpty(topic.getContent())) {
            throw new ServiceException("试题名称和内容不能为空！");
        }

        //如果原附件存在且现在没有附件或者现在的附件与原附件不同，，则删除原附件
        Topic originTopic = topicDAO.load(topic.getId());
        if (originTopic == null) {
            throw new ServiceException("entity not found![" + topic.getId() + "]");
        }
        if (originTopic.getType() == Topic.Type.LISTEN.getTypeVal()
                && !StringUtils.isEmpty(originTopic.getContent())
                && (topic.getType() != Topic.Type.LISTEN.getTypeVal() || !originTopic.getContent().equals(topic.getContent()))) {
            try {
                FileUtil.deleteFile(originTopic.getContent());
            } catch (IOException e) {
                throw new RuntimeException("remove origin file error!", e);
            }
        }

        if (topic.getType() == Topic.Type.LISTEN.getTypeVal() && FileService.isTempFile(topic.getContent())) {
            //将临时文件夹中的文件移动到正式资料文件夹
            try {
                moveAndSet(topic);
            } catch (java.io.IOException e) {
                throw new ServiceException("move file from temp dir to production dir error", e);
            }

        }

        BeanUtils.copyProperties(originTopic, topic, "name", "score", "type", "period", "content", "answer", "playtype");
        boolean r = topicDAO.edit(topic);
        if (!r) {
            throw new ServiceException("保存试题失败！");
        }
    }

    @Override
    public Topic findById(Integer tid) {
        return topicDAO.load(tid);
    }

    @Override
    public List<String> listFieldsByEid(Integer eid) {

        List<Topic> topics = topicDAO.findByEid(eid);
        return topics.stream().map(Topic::getName).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void importTopics(MultipartFile file) throws ServiceException {

        if (!file.getOriginalFilename().toUpperCase().endsWith(".ZIP")) {
            throw new ServiceException("\u5bfc\u5165\u7684\u6587\u4ef6\u5fc5\u987b\u662fzip\u6587\u4ef6\uff01");
        }

        File zipFile = saveFile(file);
        File folder = FileUtil.unzip(zipFile.getAbsolutePath());
        File excelFile = findExcelFile(folder);
        if (excelFile == null) {
            throw new ServiceException("zip\u6587\u4ef6\u4e2d\u6ca1\u6709xls\u6587\u4ef6\uff0c\u5fc5\u987b\u5305\u542b\u4e00\u4e2axls\u6587\u4ef6\uff01");
        }

        try {
            Workbook wb = Workbook.getWorkbook(excelFile);
            Sheet sheet = wb.getSheet(0);

            int rowCount = sheet.getRows();
            List<Topic> topics = new ArrayList<>(rowCount - 1);
            for (int i = 1; i < rowCount; i++) {

                String name = sheet.getCell(0, i).getContents();
                int score = Integer.parseInt(sheet.getCell(1, i).getContents());
                int type = "对照附录".equals(sheet.getCell(2, i).getContents()) ? 1 : 2;
                int period = Integer.parseInt(sheet.getCell(3, i).getContents());
                String content = sheet.getCell(4, i).getContents();
                String answer = type == 1 ? content : sheet.getCell(5, i).getContents();
                String fileName = sheet.getCell(6, i).getContents();
                int playtype = "是".equals(sheet.getCell(7, i).getContents()) ? 1 : 2;


                Topic topic = new Topic();
                topic.setName(name);
                topic.setAnswer(answer);
                if (type == 2) {
                    File f = trySaveAudioFile(fileName, folder);
                    topic.setContent(f.getName());
                    topic.setPlaytype(playtype);
                } else {
                    topic.setContent(content);
                }
                topic.setPeriod(period);
                topic.setScore(score);
                topic.setType(type);

                topics.add(topic);
            }

            topicDAO.addMulti(topics);
        } catch (IOException | BiffException e) {
            throw new ServiceException("解析xls文件失败！", e);
        }

    }

    private File trySaveAudioFile(String fileName, File folder) throws ServiceException {
        File[] files = folder.listFiles();

        assert files != null;
        for (File file : files) {
            if (fileName.equalsIgnoreCase(file.getName())) {
                return FileUtil.copyFile(file, FileService.getProductionFolder());
            }
        }
        throw new ServiceException("\u6ca1\u6709\u627e\u5230\u97f3\u9891\u6587\u4ef6{" + fileName + "}");
    }

    private File findExcelFile(File folder) {

        File[] files = folder.listFiles();

        assert files != null;
        for (File file : files) {
            if (file.getName().toUpperCase().endsWith(".XLS")) {
                return file;
            }
        }
        return null;
    }

    private static File saveFile(MultipartFile file) throws ServiceException {

        String folder = System.getProperty("java.io.tmpdir") + "\\exam";
        File destFile = new File(folder, "topic-" + new Date().getTime() + ".zip");

        FileUtil.createFile(destFile);
        try (InputStream is = file.getInputStream(); FileOutputStream fos = new FileOutputStream(destFile)) {
            StreamUtils.copy(is, fos);
        } catch (IOException e) {
            throw new ServiceException("保存zip文件失败！", e);
        }
        return destFile;
    }

    private static void moveAndSet(Topic topic) throws IOException {
        File productionFile = FileUtil.moveTemp2Pro(topic.getContent());
        topic.setContent(productionFile.getAbsolutePath());
    }
}
