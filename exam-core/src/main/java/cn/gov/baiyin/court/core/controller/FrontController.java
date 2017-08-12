package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.entity.Score;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IEsessionService;
import cn.gov.baiyin.court.core.service.IScoreService;
import cn.gov.baiyin.court.core.service.impl.EsessionService;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by WK on 2017/3/29.
 */
@Controller
@RequestMapping("/front")
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);

    private IEsessionService esessionService;
    private IScoreService scoreService;

    @Autowired
    public void setScoreService(IScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Autowired
    public void setEsessionService(IEsessionService esessionService) {
        this.esessionService = esessionService;
    }

    @RequestMapping("/checkHasDone")
    @ResponseBody
    public Msg checkHasDone(Integer tid, Integer eid) throws ServiceException {

        esessionService.checkHasDone(eid);
        //您已经参加过本次考试，不能重复参加！
        return Msg.SUCCESS;
//        return new Msg(r, "\u60a8\u5df2\u7ecf\u53c2\u52a0\u8fc7\u672c\u6b21\u8003\u8bd5\uff0c\u4e0d\u80fd\u91cd\u590d\u53c2\u52a0\uff01", null);
    }

    @RequestMapping("/getEsessions")
    @ResponseBody
    public Msg getEsessions() {

        List<ESession> eSessions = esessionService.getCurrEsessions();
        return Msg.success(eSessions);
    }

    @RequestMapping("/submitAnswer")
    @ResponseBody
    public Msg submitAnswer(String answer, Integer speed, Float accuracy, Integer tid, Integer eid, Integer esId) {

        Score score;
        try {
            score = scoreService.submitAnswer(answer, speed, accuracy, tid, eid, esId);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.success(score);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Msg logout(HttpServletRequest request) {
        request.getSession().removeAttribute(Authc.AUTH_KEY);

        return Msg.SUCCESS;
    }

    @RequestMapping("/showScoreDetail")
    @ResponseBody
    public Msg detail(Integer eid) {

        try {
            return Msg.success(scoreService.frontDetail(eid));
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/examOver")
    @ResponseBody
    public Msg examOver(Integer eid) {

        esessionService.examOver(eid);
        return Msg.SUCCESS;
    }

    @RequestMapping("/file/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String fullPath) {

        FileUtil.tryWriteFileToResponse(fullPath, response);

//        try {
//            if (response.getOutputStream() != null) {
//                response.getOutputStream().flush();
//                response.getOutputStream().close();
//            }
//        } catch (Exception e) {
//            LOGGER.error("close response error", e);
//        }
    }
}
