package com.zime.web.util;

import java.util.UUID;

/**
 * @author LJH
 * @version 1.0
 * @date 2018/1/28 13:00
 */
public class StringUtil {

    /**
     * <pre>
     * 判断是否为空，为空则返回true
     * 为空的条件：null、""、"null"
     * </pre>
     *
     * @param obj
     * @return
     */
    public static boolean isBlank(Object obj) {
        if (obj == null) {
            return true;
        }
        String str = obj.toString().trim();
        if ("".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    /**
     * <pre>
     * 判断是否不为空，不为空则返回true
     * 为空的条件：null、""、"null"
     * </pre>
     *
     * @param obj
     * @return
     */
    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }


    /**
     * 指定长度UUID
     * @param length
     * @return
     */
    public static String getUUID(int length) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        if (uuid.length() > length) {
            uuid = uuid.substring(uuid.length() - length, uuid.length());
        }
        return uuid;
    }


    /**
     * 指定长度随机数
     * @param n
     * @return
     */
    public static String random(int n) {
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, n - 1)) + "";
    }

}
