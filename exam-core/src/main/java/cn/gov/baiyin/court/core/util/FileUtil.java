package cn.gov.baiyin.court.core.util;

import cn.gov.baiyin.court.core.service.impl.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

/**
 * Created by WK on 2017/3/27.
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static void saveFile(InputStream is, OutputStream os) {

        try {

            byte[] buff = new byte[1024];
            int len;

            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            os.flush();
        } catch (Exception e) {

            throw new RuntimeException("save file error", e);
        } finally {

            try {
                is.close();
            } catch (IOException e) {
            }

            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }

    public static void deleteFile(String content) throws IOException {

        File f = new File(content);

        if (f.exists()) {
            Files.delete(f.toPath());
        }
    }

    public static String parseFileName(String fullName) {

        String sep = File.separator;

        String fileName;
        if (fullName.contains(sep)) {
            fileName = fullName.substring(fullName.lastIndexOf(sep) + 1);
        } else {
            fileName = fullName;
        }
        return fileName;
    }

    public static File moveTemp2Pro(String content) throws IOException {
        String fileName = FileUtil.parseFileName(content);
        File productionFile = new File(FileService.getProductionFolder(), fileName);
        FileUtil.saveFile(new FileInputStream(new File(content)),
                new FileOutputStream(productionFile));
        FileUtil.deleteFile(content);
        return productionFile;
    }

    public static void tryWriteFileToResponse(String fullPath, HttpServletResponse response) {
        File f = new File(fullPath);
        if (f.exists()) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + FileUtil.parseFileName(fullPath) + "");

            try (FileInputStream fis = new FileInputStream(f);) {
                FileUtil.saveFile(fis, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("download file error!", e);
            }
        } else {
            LOGGER.error("file not found~~ [" + fullPath + "]");
        }
    }
}
