package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import lombok.Data;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:38
 * @Project : user_service
 */
@Data
public class UserBookBrowse {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("browseTime")
    private OffsetDateTime browseTime;
}
