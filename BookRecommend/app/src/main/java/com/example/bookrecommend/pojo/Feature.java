package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @Author: Administrator
 * @Date: 2021/03/23 10:37
 * @Project: BookRecommend
 **/
@Data
public class Feature {
    @JsonProperty("featureId")
    private short featureId;

    @JsonProperty("featureName")
    private String featureName;
}
