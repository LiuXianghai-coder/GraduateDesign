package com.example.bookrecommend.service;

import com.example.bookrecommend.pojo.ImageURL;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 上传文件相关的服务应用，对应 SaveFileService
 * 对应端口： 9050
 *
 * @Author : LiuXianghai
 * @Date : 2021/03/04 10:09
 * @Product :  BookRecommend
 */
public interface SaveFileService {

    /**
     * 通过传入对应的输入流对象、用户 Id、文件名来保存对应的图像
     * @param file : 传入的 multipart 对象
     * @param userId ： 出入的添加这个图像的用户 ID
     *
     * @return ： 保存的图像的路径
     */
    @Multipart
    @POST("/upload")
    Call<ImageURL> uploadImage(
            @Part MultipartBody.Part file,
            @Part("userId") RequestBody userId
    );
}
