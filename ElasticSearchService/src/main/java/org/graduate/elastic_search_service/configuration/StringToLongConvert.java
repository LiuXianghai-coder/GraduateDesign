package org.graduate.elastic_search_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 23:00
 * @Project : elastic_search_service
 */
@Configuration
public class StringToLongConvert implements Converter<String, Long> {
    @Override
    public Long convert(@NonNull String source) {
        return Long.parseLong(source);
    }
}
