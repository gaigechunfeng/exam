package cn.gov.baiyin.court.core.exception;

/**
 * Created by WK on 2017/3/26.
 */
public class ServiceException extends Exception {
    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Exception e) {
        super(s, e);
    }
}
