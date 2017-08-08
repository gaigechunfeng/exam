package cn.gov.baiyin.court.core.service;

import cn.gov.baiyin.court.core.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WK on 2017/3/27.
 */
public interface IFileService {
    String save(MultipartFile file) throws ServiceException;

    void deleteFile(String file) throws ServiceException;
}
