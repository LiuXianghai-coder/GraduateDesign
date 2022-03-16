package org.graduate.elastic_search_service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.graduate.elastic_search_service.entity.SearchContent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 将请求传入的 SearchContent 参数 （json格式）转换为对应的 SearchContent 对象
 *
 * 这个组件类的存在是为了能够为 @ModelAttribute 和 @RequestParam 注解的参数提供一个
 * 将 java.lang.String 转换为 SearchContent 的策略。
 *
 * @author : LiuXianghai on 2021/2/22
 * @Created : 2021/02/22 - 10:16
 * @Project : elastic_search_service
 */
@Component
public class StringToSearchContent implements Converter<String, SearchContent> {
    @Resource(name = "JacksonMapper")
    private ObjectMapper mapper;

    @Override
    public SearchContent convert(@NonNull String source) {
        try {
            return mapper.readValue(source, SearchContent.class);
        } catch (Exception e) {
            return null;
        }
    }
}
