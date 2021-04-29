package org.graduate.savefile.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @Author : LiuXianghai
 * @Date : 2021/03/04 21:58
 * @Product :  BookRecommend
 */
@Data
public class ImageURL {
    @JsonProperty("imageUrl")
    private String imageUrl;
}
