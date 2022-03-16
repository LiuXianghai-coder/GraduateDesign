package com.example.bookrecommend.service;

import com.example.bookrecommend.pojo.Feature;
import com.example.bookrecommend.pojo.UserBookBrowse;
import com.example.bookrecommend.pojo.UserInfo;
import com.example.bookrecommend.pojo.UserShareBrowse;
import com.example.bookrecommend.sington.HttpStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 对应：UserService， 提供对书籍信息的处理接口
 * 端口：9070
 *
 * @Author: Administrator
 * @Date: 2021/03/23 10:35
 * @Project: BookRecommend
 **/
public interface UserService {
    /**
     * 获取用户可能包含的所有特征信息
     * @return ：得到用户所有的特征信息对象列表
     */
    @Headers("Content-Type: application/json")
    @GET("/feature/allFeature")
    Call<List<Feature>> allFeature();

    /**
     * 验证用户的登录信息， 首先检查用户的邮箱地址是否存在且有效，
     *  如果有效则使用邮箱与对应的加密后的密码进行配对查找。如果查找到相关的信息， 则认证成功。
     *
     * 如果没有出现对应的邮箱地址， 那么将会直接对用户的手机号和加密后的密码结对进行查找，
     *  如果查找到对应的用户， 则说明认证成功。
     *
     * @param userInfo ：输入了邮箱或者手机号以及对应的加密后的密码的用户信息对象
     * @return ： 如果认证成功， 则返回 Http 状态码 200， 否则返回 Http 状态码 202.
     */
    @Headers("Content-Type: application/json")
    @POST("/user/login")
    Call<UserInfo> userLogin(@Body UserInfo userInfo);

    /**
     * 注册用户的信息处理
     * @param userInfo ： 要注册的用户信息对象
     * @return ： 如果当前的用户名、用户手机号、用户邮箱已经被注册了， 则返回状态码 202， 如果保存成功， 则返回状态码 200
     */
    @Headers("Content-Type: application/json")
    @POST("/user/register")
    Call<HttpStatus> register(@Body UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo ： 要更新的用户信息对象
     * @return ： 处理成功， 则返回状态码 200
     */
    @Headers("Content-Type: application/json")
    @POST("/user/update")
    Call<HttpStatus> update(@Body UserInfo userInfo);

    /**
     * 按照用户输入的用户名， 查找是否存在已经注册的用户
     * @param userName ： 输入查找的用户名参数
     * @return ： 如果存在， 则返回 Http 状态码 202, 否则， 返回状态码 200
     */
    @Headers("Content-Type: application/json")
    @GET("/user/findByName")
    Call<HttpStatus> findUserInfoByName(@Query("userName") String userName);

    /**
     * 按照用户输入的电话号码查找是否存在对应的注册用户
     * @param userPhone ： 输入查找的电话号码
     * @return ： 如果该电话号码已经被注册了， 则返回 Http状态码 202， 否则， 返回状态码 200
     */
    @Headers("Content-Type: application/json")
    @GET("/user/findByPhone")
    Call<HttpStatus> findUserInfoByPhone(@Query("userPhone") String userPhone);

    /**
     * 按照用户输入的邮箱查找是否存在对应的注册用户
     * @param userEmail ： 输入查找的邮箱
     * @return ： 如果该电话号码已经被注册了， 则返回 Http状态码 202， 否则， 返回状态码 200
     */
    @Headers("Content-Type: application/json")
    @GET("/user/findByEmail")
    Call<HttpStatus> findUserInfoByEmail(@Query("userEmail") String userEmail);

    /**
     * 更新指定用户的基本信息
     * @param userInfo ： 待更新的用户信息对象
     * @return ： 如果更新成功， 返回 Http 状态码 200.
     */
    @Headers("Content-Type: application/json")
    @POST("/user/update/{userId}")
    Call<HttpStatus> updateUserInfo(@Body UserInfo userInfo, @Path("userId") Long userId);

    /**
     * 保存或更新用户对于相关书籍的浏览信息
     * @param obj ： 保存或更新的用户书籍浏览对象
     * @return ： 处理成功返回 Http 状态码 200
     */
    @Headers("Content-Type: application/json")
    @POST("/user/updateUserBookBrowse")
    Call<HttpStatus> updateUserBrowse(@Body UserBookBrowse obj);

    /**
     * 保存或更新用户对于动态信息的浏览信息对象
     * @param obj ： 保存的用户浏览动态对象
     * @return ： 处理结果，成功则返回 Http 状态码 200
     */
    @Headers("Content-Type: application/json")
    @POST("/user/updateUserShareBrowse")
    Call<HttpStatus> updateUserShareBrowse(@Body UserShareBrowse obj);

    /**
     * 通过邮箱获取对应的邮箱验证码
     * @param mailAddress ： 发送验证码的邮箱目的地址
     * @return ： 得到的验证码
     */
    @Headers("Content-Type: application/json")
    @GET("/verifyCode/mailCode")
    Call<String> getMailCode(@Query("mailAddress") String mailAddress);

    /**
     * 通过手机号夺取对应的手机验证码
     * @param phone ： 发送验证码的目的手机号
     * @return ： 得到的验证码信息
     */
    @Headers("Content-Type: application/json")
    @GET("/verifyCode/phoneCode")
    Call<String> getPhoneCode(@Query("phone") String phone);
}
