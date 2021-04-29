package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import lombok.Data;

/**
 * 显示给用户界面的基础分享信息实体类
 *
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:12
 * @Project : usershare
 */
@Data
public class UserShare {
    @JsonProperty("shareId")
    private long shareId;

    @JsonProperty("shareHeader")
    private String shareHeader;

    @JsonProperty("starNum")
    private int starNum;

    @JsonProperty("commentNum")
    private int commentNum;

    @JsonProperty("shareDate")
    private OffsetDateTime shareDate;

    @JsonProperty("userId")
    private long userId;

    private int collectionNum;

    private String shareContent;
}
