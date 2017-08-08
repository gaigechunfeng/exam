package cn.gov.baiyin.court.test;

import cn.gov.baiyin.court.core.util.HashUtil;
import org.junit.Test;

/**
 * Created by WK on 2017/3/25.
 */
public class HashUtilTest {

    @Test
    public void hash(){

        System.out.println(HashUtil.md5("11111111"));
    }

    @Test
    public void t(){

        String s = "{\"success\":true,\"errmsg\":null,\"obj\":{\"list\":[{\"id\":2,\"crtime\":\"2017325205\",\"cruser\":\"system\",\"username\":\"musicPlayer1\",\"password\":\"3d186804534370c3c817db0563f0e461\",\"photo\":null,\"name\":\"admin\",\"idcard\":null,\"permission\":100,\"type\":1,\"sex\":1,\"tableName\":\"user\"},{\"id\":3,\"crtime\":\"2017325205\",\"cruser\":\"system\",\"username\":\"musicPlayer2\",\"password\":\"3d186804534370c3c817db0563f0e461\",\"photo\":null,\"name\":\"admin\",\"idcard\":null,\"permission\":100,\"type\":1,\"sex\":1,\"tableName\":\"user\"}],\"currPage\":1,\"pageSize\":10,\"allPage\":1}}";
        System.out.println(s.length());
    }
}
