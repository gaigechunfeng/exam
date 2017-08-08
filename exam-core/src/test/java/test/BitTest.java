package test;

import org.junit.Test;

/**
 * Created by WK on 2017/3/31.
 */
public class BitTest {

    @Test
    public void t() {

        for (Object o : System.getProperties().keySet()) {
            System.out.println(o + "==" + System.getProperty(o.toString()));
        }
    }
}
