package cn.gov.baiyin.court.www;

import cn.gov.baiyin.court.core.util.CodeUtil;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.PathUtil;
import com.alibaba.druid.support.json.JSONUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WK on 2017/3/30.
 */
public class Pack {

    private static final String ENC = "UTF8";

    public static void main(String[] args) {
        pack(PathUtil.getHome(Pack.class));
    }

    private static void pack(String home) {

        String cp = "../resources;" + printLibPath(home + "/lib");
        System.out.println(cp);
        write("@set exam_classpath=" + cp, new File(home + "/bin/set-classpath.bat"));
        genLisenceInfo(home);
        executeAnt(home + "/build.xml");
    }

    private static void genLisenceInfo(String home) {

        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> m = new HashMap<>();
        m.put("genTime", now.format(DateUtil.TIME_FORMATTER));
        m.put("expireTime", now.plusDays(30).format(DateUtil.TIME_FORMATTER));

        String enc = CodeUtil.encrypt(JSONUtils.toJSONString(m));
        File lisFile = new File(home, "lisence.lsc");

        try (FileOutputStream fos = new FileOutputStream(lisFile)) {
            StreamUtils.copy(enc, CodeUtil.ENC_U8, fos);
        } catch (IOException e) {
            throw new RuntimeException("\u751f\u6210lisence\u6587\u4ef6\u5931\u8d25\uff01", e);
        }
    }

    private static void executeAnt(String xmlPath) {
        File xmlFile = new File(xmlPath);
        Project p = new Project();
        p.setUserProperty("ant.file", xmlFile.getAbsolutePath());
        p.init();
        ProjectHelper.getProjectHelper().parse(p, xmlFile);
        p.executeTarget(p.getDefaultTarget());
    }

    private static void write(String content, File file) {


        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {

            dos.write(content.getBytes(ENC));
        } catch (IOException e) {
            throw new RuntimeException("write file error", e);
        }
    }

    private static String printLibPath(String home) {

        File f = new File(home);
        if (!f.exists()) {
            throw new RuntimeException("file not found![" + home + "]");
        }
        File[] files = f.listFiles();

        if (files != null && files.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (File file : files) {
                sb.append(";../lib/").append(file.getName());
            }
            if (sb.length() > 0) {
                sb.delete(0, 1);
            }
            return sb.toString();
        }
        System.out.println("not file in lib");
        return "";
    }
}
