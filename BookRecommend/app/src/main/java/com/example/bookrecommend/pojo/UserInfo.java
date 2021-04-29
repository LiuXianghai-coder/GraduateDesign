package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * @Author: Administrator
 * @Date: 2021/03/23 10:38
 * @Project: BookRecommend
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("userPhone")
    private String userPhone;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userSex")
    private String userSex;

    @JsonProperty("headImage")
    private String headImage;

    @JsonProperty("userPassword")
    private String userPassword;

    @JsonProperty("holdingFeatures")
    private Set<Feature> holdingFeatures = new HashSet<>(); // 避免空指针选择在编译时即初始化
}
