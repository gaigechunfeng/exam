package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IFileService;
import cn.gov.baiyin.court.core.util.FileUtil;
import cn.gov.baiyin.court.core.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by WK on 2017/3/27.
 */
@Controller
@RequestMapping("/back/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private IFileService fileService;

    @Autowired
    public void setFileService(IFileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Msg upload(@RequestParam("file") MultipartFile file) {

        try {
            String filePath = fileService.save(file);

            return Msg.success(filePath);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Msg delete(String file) {

        try {
            fileService.deleteFile(file);

            return Msg.SUCCESS;
        } catch (ServiceException e) {
            return Msg.error(e);
        }
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String fullPath) {

        FileUtil.tryWriteFileToResponse(fullPath, response);

//        try {
//            if (response.getOutputStream() != null) {
//                response.getOutputStream().flush();
//                response.getOutputStream().close();
//            }
//        } catch (Exception e) {
//            LOGGER.error("close response error", e);
//        }

    }
}
