package test;

import cn.gov.baiyin.court.core.util.CodeUtil;
import org.junit.Test;

/**
 * Created by 005689 on 2017/8/8.
 */
public class CodeUtilTest {

    @Test
    public void t(){

        String s = "aaabbbcc";
        String s1 = CodeUtil.encrypt(s);
        System.out.println(s1);
        String s2 = CodeUtil.decrypt(s1);
        System.out.println(s2);
    }
}
