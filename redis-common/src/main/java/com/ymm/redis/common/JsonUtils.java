package com.ymm.redis.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() {
    }

    public static String convertJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error("json parse error", e);
        }
        return null;
    }

    public static <T> T convertObject(String json, Class<T> object) {
        try {
            return mapper.readValue(json, object);
        } catch (Exception e) {
            LOGGER.error("convertObject has exception", e);
        }
        return null;
    }

    public static <T> T convertObject(String json, Type type, Class<T> object) {
        JavaType javaType = mapper.getTypeFactory().constructType(type, object);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error("convertObject has exception", e);
        }
        return null;
    }

    public static <T> List<T> convertList(String json, Class<T> object) {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, object);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error("convertList has exception", e);
        }
        return null;
    }

    public static <T> Set<T> convertSet(String json, Class<T> object) {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(Set.class, object);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error("convertSet has exception", e);
        }
        return null;
    }

    public static <K, V> Map<K, V> convertMap(String json, Class<K> keyClass, Class<V> valueClass) {
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error("convertMap has exception", e);
        }
        return null;
    }

}
