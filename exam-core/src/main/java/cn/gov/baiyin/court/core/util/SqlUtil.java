package cn.gov.baiyin.court.core.util;

import cn.gov.baiyin.court.core.annotations.SqlIgnore;
import cn.gov.baiyin.court.core.entity.BaseEntity;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WK on 2017/3/26.
 */
public class SqlUtil {

    public static <T extends BaseEntity> SqlInfo genInsertSqlInfo(T obj) {

        Class cls = obj.getClass();
        List<Object> params = new ArrayList<>();
        List<String> fieldNames = parseFieldNames(cls);
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        for (String fieldName : fieldNames) {
            if (!"id".equals(fieldName)) {
                s1.append(",").append(fieldName);
                s2.append(",").append("?");

                Method m = getGetterMethod(cls, fieldName);
                try {
                    Object v = m.invoke(obj);
                    params.add(v);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("execute getter method error~~", e);
                }
            }
        }
        if (s1.length() > 0) {
            s1.delete(0, 1);
        }
        if (s2.length() > 0) {
            s2.delete(0, 1);
        }
        String sql = "insert into " + obj.getTableName() + "(" + s1 + ") values (" + s2 + ")";
        return new SqlInfo(sql, params);
    }

    private static Method getGetterMethod(Class cls, String fieldName) {

        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return cls.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("getGetterMethod error", e);
        }
    }

    private static List<String> parseFieldNames(Class<?> cls) {
        Method[] methods = cls.getMethods();
        List<String> strings = new ArrayList<>();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && method.getAnnotation(SqlIgnore.class) == null) {
                String fn = methodName.substring(3);
                if (!StringUtils.isEmpty(fn) && method.getParameterCount() == 1) {
                    fn = fn.substring(0, 1).toLowerCase() + fn.substring(1);
                    strings.add(fn);
                }
            }
        }
        return strings;
    }

    public static <T extends BaseEntity> SqlInfo genModifySqlInfo(T obj) {
        Class cls = obj.getClass();
        List<Object> params = new ArrayList<>();
        List<String> fieldNames = parseFieldNames(cls);
        StringBuilder s1 = new StringBuilder();
        for (String fieldName : fieldNames) {
            if (!"id".equals(fieldName)) {
                s1.append(",").append(fieldName).append("=?");

                Method m = getGetterMethod(cls, fieldName);
                try {
                    Object v = m.invoke(obj);
                    params.add(v);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("execute getter method error~~", e);
                }
            }
        }
        if (s1.length() > 0) {
            s1.delete(0, 1);
        }
        String sql = "update " + obj.getTableName() + " set " + s1 + " where id=?";
        params.add(obj.getId());
        return new SqlInfo(sql, params);
    }

    public static String genPageSql(String sql, PageInfo pageInfo) {

        int start = (pageInfo.getCurrPage() - 1) * pageInfo.getPageSize();
        return sql + " limit " + start + "," + pageInfo.getPageSize();
    }

    public static class SqlInfo {
        final String sql;
        final List<Object> params;

        SqlInfo(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getParams() {
            return params;
        }
    }
}
