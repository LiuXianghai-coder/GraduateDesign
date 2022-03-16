package org.graduate.elastic_search_service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.graduate.elastic_search_service.entity.SearchPage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 将传入的 SearchPage 参数（json形式） 转换为对应的 SearchPage 对象
 *
 * 这个组件类的存在是为了能够为 @ModelAttribute 和 @RequestParam 注解的参数提供一个
 * 将 java.lang.String 转换为 SearChPage 的策略。
 *
 * @author : LiuXianghai on 2021/2/22
 * @Created : 2021/02/22 - 10:11
 * @Project : elastic_search_service
 */
@Component
public class StringToSearchPage implements Converter<String, SearchPage> {
    @Resource(name = "JacksonMapper")
    private ObjectMapper mapper;

    @Override
    public SearchPage convert(@NonNull String s) {
        try {
            return mapper.readValue(s, SearchPage.class);
        } catch (Exception e) {
            return null;
        }
    }
}
