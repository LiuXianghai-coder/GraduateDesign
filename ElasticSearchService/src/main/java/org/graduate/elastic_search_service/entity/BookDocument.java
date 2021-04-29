package org.graduate.elastic_search_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 显示给用户搜索界面的 Book 对象
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/10
 * Time: 下午9:33
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BookDocument {
    /*
        书籍的 ISBN 号。
        为了支持 isbn 对应的 ElasticSearch，
        所以这里需要把原来为 Long 类型的 ISBN 改为 String 类型的。
        因为数值型的 ISBN 不支持使用正则表达式类匹配
     */
    @JsonProperty("isbn")
    private String isbn;

    /*
        书籍名称
     */
    @JsonProperty("bookName")
    private String bookName;

    /*
        作者姓名， 由于一本书存在多个作者， 因此这里的类型为 List
     */
    @JsonProperty("authors")
    private List<AuthorSimple> authors;

    /*
        选一张有代表性的图片地址作为这本书的样子， 吸引客户 ：）
     */
    @JsonProperty("imageUrl")
    private String imageUrl;
}