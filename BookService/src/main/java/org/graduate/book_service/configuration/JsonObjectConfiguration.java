package org.graduate.book_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/24
 * Time: 上午11:50
 */

@Configuration
public class JsonObjectConfiguration {
    @Bean(name = "JacksonMapper")
    public ObjectMapper jacksonMapper() {
        return new ObjectMapper().findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
