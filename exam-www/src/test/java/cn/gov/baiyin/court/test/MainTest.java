package cn.gov.baiyin.court.test;

import cn.gov.baiyin.court.core.service.IScoreService;
import cn.gov.baiyin.court.core.util.Utils;
import cn.gov.baiyin.court.www.util.WebUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by WK on 2017/3/25.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:application-config.xml",
        "classpath:mvc-config.xml"})
public class MainTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private IScoreService scoreService;

    @BeforeClass
    public static void doBefore() {
        Utils.setApp(WebUtil.APP);
    }

    @Test
    public void test() {

        System.out.println(wac);

        System.out.println(scoreService.exportDb2Sql());
    }
}
