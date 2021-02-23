package com.nurkiewicz.elasticflux;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 黄念
 * @version 1.0
 * @Description
 * @date 2020/12/4
 */

public class ParamsUtils {

    public static void validateMap(Map map, String... strArr) throws Exception {
        //返回结果
        for (String s : strArr) {
            validateNull(map.get(s), s);
        }
    }

    public static <T> void validateNull(T obj, String... fields) throws Exception {
        Class<?> objClass = obj.getClass();
        for (String field : fields) {
            Method method = objClass.getMethod("get" + (char) (field.charAt(0) - 32) + field.substring(1));
            validateNull(method.invoke(obj), field);
        }
    }

    public static List<Integer> strToIntList(String s) {
        return strToIntList(s, ",");
    }

    public static List<Integer> strToIntList(String str, String separator) {
        if (StringUtils.isBlank(str)) return new ArrayList<>();
        return Lists.newArrayList(
                Ints.stringConverter().convertAll(strToList(str, separator)));
    }

    public static List<String> strToList(String str) {
        if (StringUtils.isBlank(str)) return new ArrayList<>();
        return strToList(str, ",");
    }

    public static List<String> strToList(String str, String separator) {
        if (StringUtils.isBlank(str)) return new ArrayList<>();
        return Splitter.on(separator).trimResults().omitEmptyStrings().splitToList(str);
    }

    //构造sql in查询条件,前后添加上 "'" 防止sql注入.
    public static String inCriteria(String str) {
        if (StringUtils.isBlank(str)) return null;
        return "'" + str.replaceAll(",", "','") + "'";
    }
}
