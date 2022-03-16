package org.graduate.elastic_search_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索的内容对象
 *
 * @author : LiuXianghai on 2021/2/21
 * @Created : 2021/02/21 - 16:43
 * @Project : elastic_search_service
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchContent {
    // 输入的搜索文本内容的查找类型
    @JsonProperty("type")
    private ContentType type;

    // 输入的查找条件
    @JsonProperty("content")
    private String content;
}
