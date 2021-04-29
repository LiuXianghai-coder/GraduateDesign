package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 经过简化的 Author 类， 这个类的存在是为了简化作者信息。
 * 这个类消除了多余的作者介绍信息字段，使得能够更加节省空间以及加快执行速度。
 *
 * @Created: LiuXianghai
 * @Date: 2021年2月20日10:58:33
 * @Product: BookRecommend
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthorSimple {
    /*
        作者的ID 信息，
        这个信息可以用于再次查找指定的作者对应的书籍信息等
     */
    @JsonProperty("authorId")
    private Number authorId;

    /*
        作者姓名。。。。。
     */
    @JsonProperty("authorName")
    private String authorName;
}
