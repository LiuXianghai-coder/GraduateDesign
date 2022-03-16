package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */

@Data
public class UserBookComment {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("commentId")
    private int commentId;

    @JsonProperty("commentContent")
    private String commentContent;

    @JsonProperty("commentDate")
    private OffsetDateTime commentDate;

    @JsonProperty("userInfo")
    private BasicUserInfo userInfo = new BasicUserInfo();
}
