package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.Examine;
import cn.gov.baiyin.court.core.entity.Topic;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IExamineService;
import cn.gov.baiyin.court.core.service.IScoreService;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.service.impl.FileService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.VelocityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/31.
 */
@Controller
@RequestMapping("/back/score")
public class ScoreController {

    private IScoreService scoreService;
    private ITopicService topicService;
    private IExamineService examineService;

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

    @Autowired
    public void setTopicService(ITopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    public void setScoreService(IScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Msg listPagination(PageInfo pageInfo, String pos, Integer eid) {

        return Msg.success(scoreService.listPagination(pageInfo, pos, eid));
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Msg detail(Integer uid, Integer eid) {

        try {
            return Msg.success(scoreService.detail(uid, eid));
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/reExam")
    @ResponseBody
    public Msg reExam(Integer uid, Integer eid) {

        try {
            scoreService.reExam(uid, eid);
            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }


    @RequestMapping("/export/detail")
    @ResponseBody
    public void exportDetail(HttpServletResponse response, PageInfo pageInfo, String pos, Integer eid) {

        pageInfo = scoreService.listPagination(pageInfo, pos, eid);

        Map<String, Object> context = new HashMap<>();
        context.put("list", pageInfo.getList());
        context.put("fields", topicService.listFieldNameByEid(eid));

        String s = VelocityUtil.parse("score", context);

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + genScoreExcelFileName() + "");
        try {
            response.getOutputStream().write(s.getBytes("utf-8"));
        } catch (IOException e) {
            throw new RuntimeException("download score excel error", e);
        }
    }

    @RequestMapping("/export/db")
    public void exportDb(HttpServletResponse response) {
        String s = scoreService.exportDb2Sql();

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=db-" + new Date().getTime() + ".sql");
        try {
            response.getOutputStream().write(s.getBytes("utf-8"));
        } catch (IOException e) {
            throw new RuntimeException("download score excel error", e);
        }
    }

    @RequestMapping("/export/dataUpload")
    public void dataUpload(HttpServletResponse response, Integer eid, String pos) throws ServiceException {

        File zipFile = scoreService.genDataUploadZip(eid, pos);

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipFile.getName());
        try (FileInputStream fis = new FileInputStream(zipFile)) {
            StreamUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("download score excel error", e);
        }
    }

    private static String genScoreExcelFileName() {

        String fn = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return "score_" + fn + ".xls";
    }

    @RequestMapping("/statistic")
    @ResponseBody
    public Msg statistic(Integer eid, String pos) {

        return Msg.success(scoreService.statistic(eid, pos));
    }

    @RequestMapping("/importEnc")
    @ResponseBody
    public Msg importEnc(HttpServletResponse response, MultipartFile file) throws ServiceException {

        File f = scoreService.importEnc(file);

        return Msg.success(f.getName());
    }

    @RequestMapping("/downloadDec")
    public void downloadDec(HttpServletResponse response, String fileName) throws ServiceException {

        File f = new File(FileService.getTempFolder(), fileName);

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        try (FileInputStream fis = new FileInputStream(f)) {
            StreamUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("download score excel error", e);
        }
    }
}
