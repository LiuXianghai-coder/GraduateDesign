package com.example.bookrecommend.service;

import com.example.bookrecommend.pojo.ShareEntity;
import com.example.bookrecommend.pojo.UserShare;
import com.example.bookrecommend.pojo.UserShareContent;
import com.example.bookrecommend.sington.HttpStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 与用户动态有关的服务内容， 对应应用： UserShareService
 * 对应端口：9040
 *
 * @Author: Administrator
 * @Date: 9:16
 * @Project: BookRecommend
 **/
public interface UserShareService {
    /**
     * 保存用户的动态信息
     * @param userShareContent ：带保存的用户动态内容
     * @return ： 处理结果， 成功则返回 Http 状态码 200
     */
    @Deprecated
    @Headers("Content-Type: application/json")
    @POST("/shareContent/saveUserShareContent")
    Call<String> saveUserShareContent(@Body UserShareContent userShareContent);

    @Headers("Content-Type: application/json")
    @POST("/shareContent/saveUserShare")
    Call<HttpStatus> saveUserShare(@Body UserShare userShare);

    /***
     * 修改用户对于对应的用户动态的收藏信息。
     * @param shareId ： 对应的动态 ID
     * @param userId ： 对应的用户 ID
     * @return ： 处理结果， 成功返回 Http 状态码 200， 否则返回 Http 状态码 202
     */
    @Headers("Content-Type: application/json")
    @POST("/shareContent/updateCollections/{shareId}/{userId}")
    Call<String> updateCollections(@Path("shareId") Long shareId, @Path("userId") Long userId);

    /**
     * 更新用户对于指定动态的点赞状态
     * @param shareId ： 点赞的动态 ID
     * @param userId ： 点赞的用户 ID
     * @return ： 处理结果， 成功则返回 Http 状态码 200， 否则返回 Http 状态码 202
     */
    @Headers("Content-Type: application/json")
    @POST("/shareContent/updateStar/{shareId}/{userId}")
    Call<String> updateStar(@Path("shareId") Long shareId, @Path("userId") Long userId);

    /**
     * 增加用户对于指定动态的评论信息
     * @param shareId ： 对应的动态 ID
     * @param userId ： 对应的用户 Id
     * @param commentContent ： 评论内容
     * @return ： 处理结果， 成功则返回 HTTP状态码 200
     */

    @Headers("Content-Type: application/json")
    @POST("/shareContent/addComment/{shareId}/{userId}")
    Call<String> addComment(@Path("shareId") Long shareId, @Path("userId") Long userId,
                            @Body String commentContent);

    /**
     * 得到默认的一页的用户动态基础信息列表
     * @param userId ： 当前访问的用户 ID， 这个是进行排序和检索的基石，
     *               也是界面显示的一个重要因素
     * @return ：得到的基础用户动态信息列表
     */
    @GET("/userShare/defaultUserShares/{userId}")
    Call<List<ShareEntity>> defaultUserShares(@Path("userId") Long userId);
}
