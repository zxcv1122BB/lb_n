package com.lb.sys.tools;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SuppressWarnings("serial")
public class MyObjectMapper extends ObjectMapper {

    public MyObjectMapper(){
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
