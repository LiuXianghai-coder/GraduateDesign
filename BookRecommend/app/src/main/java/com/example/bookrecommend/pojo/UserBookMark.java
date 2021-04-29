package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: Administrator
 * @Date: 8:53
 * @Project: BookRecommend
 **/
@Data
public class UserBookMark implements Serializable {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("score")
    private short score;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("markDate")
    private OffsetDateTime markDate;
}
