package cn.gov.baiyin.court.core.annotations;

import java.lang.annotation.*;

/**
 * Created by WK on 2017/3/28.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SqlIgnore {
}
