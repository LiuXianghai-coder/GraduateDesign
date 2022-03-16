package org.graduate.elastic_search_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ObjectMapper Beans
 *
 * @author : LiuXianghai on 2021/2/22
 * @Created : 2021/02/22 - 10:12
 * @Project : elastic_search_service
 */
@Configuration
public class MapperBeans {
    @Bean(name = "JacksonMapper")
    public ObjectMapper jacksonMapper() {
        return new ObjectMapper();
    }
}
