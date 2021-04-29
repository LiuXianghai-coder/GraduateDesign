package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import lombok.Data;

/**
 * 动态信息的基础元素对象
 *
 * @Author : LiuXianghai
 * @Date : 2021/02/24 21:01
 * @Product :  BookRecommend
 */
@Data
public class ShareEntity {
    /*
        创建这个动态的用户的 ID，
        这个字段属性的存在是为了能够为将来的推荐系统进行进一步的分析
     */
    @JsonProperty("createdUserId")
    private long createdUserId;

    /*
        当前动态元素对象的动态 ID，
        使用这个 ID 才能进行对应的导航， 同时获取对应的详细动态信息
     */
    @JsonProperty("shareId")
    private long shareId;

    /*
        这个动态信息的标题
     */
    @JsonProperty("shareHead")
    private String shareHead;

    /*
        这个动态被点赞的次数
     */
    @JsonProperty("starNum")
    private int starNum;

    /*
        这个动态被评论的次数
     */
    @JsonProperty("commentNum")
    private int commentNum;

    /*
        这个动态创建的时间，
        使用 OffsetDateTime 是为了与对应的时区进行匹配
     */
    @JsonProperty("shareDate")
    private OffsetDateTime shareDate;

    /*
        这个动态被收藏的次数
     */
    @JsonProperty("collectionNum")
    private int collectionNum;

    /*
        当前登录的用户是否已经点赞了这个动态
     */
    @JsonProperty("star")
    private boolean star;

    /*
        当前登录的用户是否已经收藏了该动态
     */
    @JsonProperty("collection")
    private boolean collection;

    /*
        这个动态中的一个有代表性的图片得到地址
     */
    @JsonProperty("imageUrl")
    private String imageUrl;
}
