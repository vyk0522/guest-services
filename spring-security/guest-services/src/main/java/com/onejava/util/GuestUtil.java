package com.onejava.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public class GuestUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String getObjectAsJsonString(Object object) {
        String jsonString;
        try {
            jsonString = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    public static <T> T getObjectFromJson(String jsonString, Class<T> type) {
        try {
            return getObjectMapper().readValue(jsonString, type); // Guest guest = GuestUtil.getObjectFromJson(jsonString, Guest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertValue(Object data, Class<T> eventDataClass) {
        return getObjectMapper().convertValue(data, eventDataClass);
    }

    public static <T> T convertValue(Object data, TypeReference<T> typeReference) {
        return getObjectMapper().convertValue(data, typeReference);
    }

}
