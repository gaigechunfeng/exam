package cn.gov.baiyin.court.www;

import cn.gov.baiyin.court.core.service.impl.FileService;
import cn.gov.baiyin.court.core.util.Utils;
import cn.gov.baiyin.court.www.util.JettyUtil;
import cn.gov.baiyin.court.www.util.WebUtil;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by WK on 2017/3/24.
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Utils.setApp(WebUtil.APP);

        String webCtx = WebUtil.APP.getString("web.context", "/www");
        int port = WebUtil.APP.getInt("web.port", 8080);
        String homePath = WebUtil.APP.getString("web.home", WebUtil.HOME);

        List<ServletContextListener> listeners = new ArrayList<>();
        ServletHandler servletHandler = new ServletHandler();

        JettyUtil.runJetty(webCtx, port, homePath, listeners, servletHandler);

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            LOGGER.info("开始执行定时清理任务！");
            long st = System.currentTimeMillis();
            int count = FileService.cleanTempFolder();
            LOGGER.info("本次清除任务完成！共删除文件" + count + "个！耗时" + (System.currentTimeMillis() - st) + "ms");
        }, WebUtil.APP.getInt("clean.task.delay", 1), WebUtil.APP.getInt("clean.task.period", 600), TimeUnit.MINUTES);
    }
}
