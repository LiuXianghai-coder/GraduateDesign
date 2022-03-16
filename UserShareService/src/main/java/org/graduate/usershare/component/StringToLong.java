package org.graduate.usershare.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 15:16
 * @Project : usershare
 */
@Component
public class StringToLong implements Converter<String, Long> {
    @Override
    public Long convert(@NonNull String s) {
        return Long.parseLong(toString());
    }
}
