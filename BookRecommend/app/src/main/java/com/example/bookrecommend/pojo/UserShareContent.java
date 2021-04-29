package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Administrator
 * @Date: 9:14
 * @Project: BookRecommend
 **/
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserShareContent {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("shareContent")
    private String shareContent;
}
