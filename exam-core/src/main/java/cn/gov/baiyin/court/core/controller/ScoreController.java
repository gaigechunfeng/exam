package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IScoreService;
import cn.gov.baiyin.court.core.service.ITopicService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.VelocityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WK on 2017/3/31.
 */
@Controller
@RequestMapping("/back/score")
public class ScoreController {

    private IScoreService scoreService;
    private ITopicService topicService;

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
        context.put("fields", topicService.listFieldsByEid(eid));

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
    @ResponseBody
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

    private static String genScoreExcelFileName() {

        String fn = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return "score_" + fn + ".xls";
    }

    @RequestMapping("/statistic")
    @ResponseBody
    public Msg statistic(Integer eid) {

        return Msg.success(scoreService.statistic(eid));
    }
}
