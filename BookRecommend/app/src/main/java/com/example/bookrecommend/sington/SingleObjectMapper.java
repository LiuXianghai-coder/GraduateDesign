package com.example.bookrecommend.sington;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public enum SingleObjectMapper {
    DEFAULT_INSTANCE;

    private final ObjectMapper mapper;

    SingleObjectMapper() {
        this.mapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
