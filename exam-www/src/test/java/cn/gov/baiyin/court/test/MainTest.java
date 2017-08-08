package cn.gov.baiyin.court.test;

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

    @Test
    public void test() {

        System.out.println(wac);
    }
}
