package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IFileService;
import cn.gov.baiyin.court.core.util.DateUtil;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.PathUtil;
import cn.gov.baiyin.court.core.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WK on 2017/3/27.
 */
@Service
public class FileService implements IFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private static final int BEFORE = Utils.getApp().getInt("clean.task.period", 600);

    @Override
    public String save(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("file is empty");
        }

        File destFile = getTempFolder();
        File saveFile = new File(destFile, "temp-" + System.currentTimeMillis() + "." + FileUtil.getFileExt(file.getOriginalFilename()));

        try {
            FileUtil.saveFile(file.getInputStream(), new FileOutputStream(saveFile));
        } catch (IOException e) {
            throw new RuntimeException("save file error");
        }
        return saveFile.getName();
    }

    public static File getTempFolder() {
        File f = new File(Utils.getApp().getString("temp.path", Utils.getHome() + "\\temp"));
        if (!f.exists()) {
            boolean r = f.mkdirs();
            if (!r) {
                throw new RuntimeException("create temp folder error");
            }
        }
        return f;
    }

    @Override
    public void deleteFile(String file) throws ServiceException {

        if (StringUtils.isEmpty(file)) {
            throw new RuntimeException("参数错误！[file]");
        }
        File f = new File(file);
        if (!f.exists()) {
            throw new RuntimeException("文件不存在！[" + file + "]");
        }

        if (!validatePermission(f)) {
            throw new RuntimeException("\u6539\u6587\u4ef6\u975e\u9879\u76ee\u4e0a\u4f20\u8def\u5f84\uff0c\u4e0d\u5141\u8bb8\u5220\u9664\uff01");
        }
        try {
            Files.delete(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException("删除失败！[" + e.getMessage() + "]", e);
        }
    }

    private boolean validatePermission(File f) {

        File pFolder = getProductionFolder();
        File tFolder = getTempFolder();

        File folder = f.getParentFile();
        return folder.equals(pFolder) || folder.equals(tFolder);
    }

    public static File getProductionFolder() {
        File f = new File(Utils.getApp().getString("upload.path", Utils.getHome() + "\\files"));
        if (!f.exists()) {
            f.mkdirs();
//            if (!r) {
//                throw new RuntimeException("create production folder error!");
//            }
        }
        return f;
    }

    public static boolean isTempFile(String fileName) {

//        File tf = getTempFolder();
//        File f = new File(tf, fileName);

        return fileName.startsWith("temp-");
    }

    public static int cleanTempFolder() {
        File folder = getTempFolder();

        AtomicInteger count = new AtomicInteger(0);
        long deadline = System.currentTimeMillis() - BEFORE * 60 * 1000;
        try {
            Files.list(folder.toPath())
                    .filter(path -> path.toFile().lastModified() < deadline)
                    .forEach(path -> {
                        FileUtil.deleteFile(path.toFile());
                        count.incrementAndGet();
                    });
        } catch (IOException e) {
            LOGGER.error("clean function error!", e);
        }
        return count.get();
    }
}
