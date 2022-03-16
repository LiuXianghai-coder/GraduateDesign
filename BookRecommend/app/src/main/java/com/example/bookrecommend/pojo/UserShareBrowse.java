package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:38
 * @Project : user_service
 */
@Data
public class UserShareBrowse {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("shareId")
    private long shareId;

    @JsonProperty("browseTime")
    private OffsetDateTime browseTime;
}
