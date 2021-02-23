package com.nurkiewicz.elasticflux;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

/**
 * @author 空山
 * @version 1.0
 * @description
 * @time 2020/11/12 10:42 上午
 */
//@Slf4j
public class JsonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 定义日期的格式
     */
    private final static  String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的属性全部加入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认的timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空Bean转换json
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //统一所有的日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        //忽略在 字符串中穿在，在java 类中不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 对象转字符串
     *
     * @param obj 任意对象
     * @return json字符串
     */
    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //log.error("parse object to string error :{}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转字符串 (美化)
     *
     * @param obj 任意对象
     * @return json字符串 （美化）
     */
    public static <T> String objToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // log.error("parse object to string error :{}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转成任意对象
     *
     * @param str   json字符串
     * @param clazz 对象类
     * @return 返回对象
     */
    @SneakyThrows
    public static <T> T stringToObj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        return objectMapper.readValue(str, clazz);
    }

}
