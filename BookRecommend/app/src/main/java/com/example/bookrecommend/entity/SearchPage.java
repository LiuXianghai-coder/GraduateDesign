package com.example.bookrecommend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

/**
 * 查找的条件对象， 包括相关的一些分页的属性信息
 *
 * @author: LiuXianghai
 * @Time: 2021年2月20日22:27:54
 */
@Data
public class SearchPage implements Serializable {
    /*
        查找的开始位置
     */
    @JsonProperty("startLocation")
    private Integer startLocation;

    /*
        查找的记录条数， 默认为 10000，这是因为一般的应用用户都不会一次性看 10000 条数据
     */
    @JsonProperty("size")
    private Integer size = 10000;

    /*
        排序的条目信息
     */
    @JsonProperty("sortCol")
    private String sortCol;
}
