package org.graduate.elastic_search_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应的按照作者 ID 来查找对应书籍的文档对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/13
 * Time: 上午10:49
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthorDocument {
    // 作者 ID
    @JsonProperty("authorId")
    private Number authorId;

    // 该 ID 对应的书籍对象
    @JsonProperty("book")
    private BookDocument book;
}
