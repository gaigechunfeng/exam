package cn.gov.baiyin.court.core.mvc;

import cn.gov.baiyin.court.core.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by WK on 2017/3/29.
 */
@ControllerAdvice
public class CommAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommAdvice.class);

    @ExceptionHandler
    @ResponseBody
    public Msg hanldeException(Exception e) {

        LOGGER.error(e.getMessage(), e);
        return Msg.error(e);
    }
}
