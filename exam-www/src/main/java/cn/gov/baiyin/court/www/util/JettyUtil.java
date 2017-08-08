package cn.gov.baiyin.court.www.util;

import cn.gov.baiyin.court.core.util.Utils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.List;

/**
 * Created by WK on 2017/3/24.
 */
public class JettyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyUtil.class);

    private static final int poolSize = Utils.getApp().getInt("pool.size", 500);

    public static void runJetty(String webCtx, int port, String homePath, List<ServletContextListener> listeners, ServletHandler servletHandler) {

        Server server = createJetty(webCtx, port, homePath, listeners, servletHandler);

        try {
            server.start();

            LOGGER.info("jetty server started at port " + port + ",context path " + webCtx + "");
        } catch (Exception e) {
            throw new RuntimeException("start web application error", e);
        }

    }

    private static Server createJetty(String webCtx, int port, String homePath, List<ServletContextListener> listeners, ServletHandler servletHandler) {

        String webPath = homePath + "/web";
        String workPath = homePath + "/work";

        final Server server = new Server(new QueuedThreadPool(poolSize));
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        server.setStopAtShutdown(true);

        WebAppContext context = new WebAppContext(webPath, webCtx);
        context.setServer(server);
        context.setTempDirectory(new File(workPath));
        context.getSessionHandler().setMaxInactiveInterval(7200);//Session过期时间2小时

        if (!CollectionUtils.isEmpty(listeners)) {
            for (ServletContextListener listener : listeners) {
                context.addEventListener(listener);
            }
        }
        if (servletHandler != null) {
            context.setServletHandler(servletHandler);
        }
        server.setHandler(context);
        return server;
    }
}
