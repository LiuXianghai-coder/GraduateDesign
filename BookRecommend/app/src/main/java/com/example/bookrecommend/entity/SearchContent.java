package com.example.bookrecommend.entity;

import com.example.bookrecommend.sington.ContentType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索内容的对象， 包括输入搜索内容的类型、搜索内容
 *
 * @Author : LiuXianghai
 * @Date : 2021/02/22 15:35
 * @Product :  BookRecommend
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchContent {
    // 输入的搜索文本内容的查找类型
    @JsonProperty("type")
    private ContentType type;

    // 输入的查找条件
    @JsonProperty("content")
    private String content;
}
