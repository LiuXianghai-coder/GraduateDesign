package org.graduate.service.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author : LiuXianghai on 2021/3/21
 * @Created : 2021/03/21 - 15:05
 * @Project : service
 */
@Component
public class StringToLongConvert implements Converter<String, Long> {
    @Override
    public Long convert(@NonNull String source) {
        return Long.parseLong(source);
    }
}
