package org.graduate.elastic_search_service.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 显示给用户搜索界面的 Book 对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/10
 * Time: 下午9:33
 */
@Data
public class Book {
    /*
        书籍的 ISBN 号。
        原则上， ISBN 应当为 Long 类型的，
        但是为了配合 ElasticSearch 的搜索， 因此这里改为 String 类型的
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
    private List<Author> authors;

    /*
        选一张有代表性的图片地址作为这本书的样子， 吸引客户 ：）
     */
    @JsonProperty("imageUrl")
    private String imageUrl;
}
