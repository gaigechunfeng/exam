package cn.gov.baiyin.court.core.util;

import cn.gov.baiyin.court.core.service.impl.FileService;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.Enumeration;

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

    public static void createFile(File destFile) {
        File f = destFile.getParentFile();
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            destFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("createFile error", e);
        }
    }

    public static File unzip(String path) {
        int count;
        String savepath;

        File file;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        savepath = path.substring(0, path.lastIndexOf(".")) + File.separator; //保存解压文件目录
        File savedFile = new File(savepath); //创建保存目录
        FileUtil.creatFolder(savedFile);

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(path, "gbk"); //解决中文乱码问题
            Enumeration<?> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                byte[] buf = new byte[2048];

                ZipEntry entry = (ZipEntry) entries.nextElement();

                String filename = entry.getName();
                boolean ismkdir = false;
                if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
                    ismkdir = true;
                }
                filename = savepath + filename;

                if (entry.isDirectory()) { //如果是文件夹先创建
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                if (!file.exists()) { //如果是目录先创建
                    if (ismkdir) {
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                    }
                }
                file.createNewFile(); //创建文件

                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, 2048);

                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();

                is.close();
            }

            zipFile.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return savedFile;
    }

    private static void creatFolder(File savedFile) {
        if (!savedFile.exists()) {
            savedFile.mkdirs();
        }
    }

    public static File copyFile(File file, File productionFolder) {
        if (!file.exists()) {
            throw new RuntimeException("file not exist{" + file + "}");
        }
        creatFolder(productionFolder);

        File f = new File(productionFolder,
                "" + new Date().getTime() + "." + getFileExt(file));
        createFile(f);
        try (FileInputStream fis = new FileInputStream(file); FileOutputStream fos = new FileOutputStream(f)) {
            StreamUtils.copy(fis, fos);
            return f;
        } catch (IOException e) {
            throw new RuntimeException("copy file error", e);
        }
    }

    private static String getFileExt(File file) {
        String fileName = file.getName();
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }
}
