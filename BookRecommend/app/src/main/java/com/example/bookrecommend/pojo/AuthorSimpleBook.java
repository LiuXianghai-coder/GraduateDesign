package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 作者的 ID 和 Book 对象的组合对象，
 * 这是由于在将作者 ID 和书籍对象进行绑定放入到 ElasticSearch 文档对象中时为了更加快速地按照作者的 ID
 * 来查找对应的书籍对象造成的。因此在这里必须沿用之前的方式来获取对应的文档对象
 *
 * @Created: LiuXianghai
 * @Date: 2021年2月20日11:06:49
 * @Product: BookRecommend
 */

@Data
public class AuthorSimpleBook {
    /*
        作者 ID， 查找的条件，还只能设置为 Number 类型的对象 :(
     */
    @JsonProperty("authorId")
    private Number authorId;

    /*
        查找到的书籍对象
     */
    @JsonProperty("book")
    private BookEntity book;
}
