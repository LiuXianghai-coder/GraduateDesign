package org.graduate.elastic_search_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 显示给用户动态界面的基础数据信息对象
 *
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:23
 * @Project : usershare
 */
@Data
public class BasicShare {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("shareId")
    private Long shareId;

    /*
        这个动态动态的标题信息
     */
    @JsonProperty("shareHead")
    private String shareHead;

    /*
        点赞数
     */
    @JsonProperty("starNum")
    private Integer starNum;

    /*
        评论数
     */
    @JsonProperty("commentNum")
    private Integer commentNum;

    /*
        随机的动态信息的图像
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /*
        收藏数
     */
    @JsonProperty("collectionNum")
    private Integer collectionNum;

    /*
        当前用户是否已经点赞了这个动态
        默认为 false
     */
    @JsonProperty("isStar")
    private Boolean isStar = false;

    /*
        当前用户是否已经收藏了该动态
        默认为 false
     */
    @JsonProperty("isCollect")
    private Boolean isCollect = false;
}
