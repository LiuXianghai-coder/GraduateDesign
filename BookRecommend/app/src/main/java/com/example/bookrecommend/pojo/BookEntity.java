package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 显示给初始主界面的基本的书籍信息。
 * 包括：书籍图像、书籍名称、作者名称等
 *
 * @Created: LiuXianghai
 * @Date: 2021年2月20日11:01:40
 * @Product: BookRecommend
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BookEntity {
    /*
        该书籍的 ISBN， 书籍的为唯一标识
     */
    @JsonProperty("isbn")
    private Long isbn;

    /*
        该 ISBN 对应的书籍名称
     */
    @JsonProperty("bookName")
    private String bookName;

    /*
        该书籍对应的作者。由于这是一个单方面的一对多的关系，
        因此这里使用 List 类型来存储
     */
    @JsonProperty("authors")
    private List<AuthorSimple> authors;

    /*
        该书籍对应的一个能够标识这本书籍的图像的地址。
        这个地址的形式应当形如：http://****.***.** /
        以这种形式来通过网络来加载对应的图片信息
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /*
        作者信息列表的字符串表示形式
     */
    private String authorsString;
}
