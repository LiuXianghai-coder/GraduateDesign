package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:48
 */

@Data
public class BookDetail {
    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("introduction")
    private String introduction;

    // 该书籍的平均分
    @JsonProperty("bookScore")
    private Double bookScore = 0.0;

    // 该书籍打了 5 分的人数
    @JsonProperty("fiveScoreNum")
    private Integer fiveScoreNum = 0;

    // 该书籍打了 4 分的人数
    @JsonProperty("fourScoreNum")
    private Integer fourScoreNum = 0;

    // 该书籍打了 3 分的人数
    @JsonProperty("threeScoreNum")
    private Integer threeScoreNum = 0;

    // 该书籍打了 2 分的人数
    @JsonProperty("twoScoreNum")
    private Integer twoScoreNum = 0;

    // 该书籍打了 1 分的人数
    @JsonProperty("oneScoreNum")
    private Integer oneScoreNum = 0;

    @JsonProperty("publisherInfo")
    private String publisherInfo;

    @JsonProperty("authorList")
    private Set<Author> authorList;

    @JsonProperty("bookImage")
    private String bookImage = "";
}
