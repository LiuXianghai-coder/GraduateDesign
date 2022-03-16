package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/4
 * Time: 下午7:28
 */

@Data
public class BasicUserInfo {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userSex")
    private String userSex;

    @JsonProperty("headImage")
    private String headImage;
}
