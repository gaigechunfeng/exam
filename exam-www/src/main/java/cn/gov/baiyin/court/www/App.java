package cn.gov.baiyin.court.www;

import cn.gov.baiyin.court.core.service.impl.FileService;
import cn.gov.baiyin.court.core.util.CodeUtil;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.Utils;
import cn.gov.baiyin.court.www.util.JettyUtil;
import cn.gov.baiyin.court.www.util.WebUtil;
import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.collections.MapUtils;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by WK on 2017/3/24.
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Utils.setApp(WebUtil.APP);
        Utils.setHome(WebUtil.HOME);

        checkLisence();

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

    private static void checkLisence() {

        File lisFile = new File(WebUtil.HOME, "lisence.lsc");
        if (!lisFile.exists() || !lisFile.isFile()) {
            throw new RuntimeException("lisence\u6587\u4ef6\u4e0d\u5b58\u5728\uff01");
        }
        try (FileInputStream fis = new FileInputStream(lisFile)) {
            String s = StreamUtils.copyToString(fis, CodeUtil.ENC_U8);
            Map<String, Object> m = (Map<String, Object>) JSONUtils.parse(CodeUtil.decrypt(s));
            String expireTime = MapUtils.getString(m, "expireTime", "");
            if (StringUtils.isEmpty(expireTime)) {
                throw new RuntimeException("\u8fc7\u671f\u65f6\u95f4\u4e3a\u7a7a\uff01");
            }
            LocalDateTime dateTime = LocalDateTime.parse(expireTime, DateUtil.TIME_FORMATTER);
            if (LocalDateTime.now().isAfter(dateTime)) {
                throw new RuntimeException("\u5e8f\u5217\u53f7\u5df2\u8fc7\u671f\uff01\u8fc7\u671f\u65f6\u95f4\uff1a" + expireTime);
            }
        } catch (IOException e) {
            throw new RuntimeException("\u5e8f\u5217\u53f7\u9519\u8bef\uff01" + e.getMessage(), e);
        }
    }
}
