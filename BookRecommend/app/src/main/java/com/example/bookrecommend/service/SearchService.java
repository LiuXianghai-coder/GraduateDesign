package com.example.bookrecommend.service;

import com.example.bookrecommend.entity.ResultObject;
import com.example.bookrecommend.entity.SearchPage;
import com.example.bookrecommend.pojo.AuthorSimpleBook;
import com.example.bookrecommend.pojo.BookEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 与搜索有关的功能，对应应用：SearchService
 * 接口：9030
 *
 * @Author: Administrator
 * @Date: 2021/03/23 10:14
 * @Project: BookRecommend
 **/
public interface SearchService {
    /**
     * 搜索书籍对象的访问接口
     * @param searchPage ： 查询的 SearchPage 对象参数
     * @param searchContent ： 搜索内容的 SearchContent 对象参数
     * @return : 得到的搜索结果对象
     */
    @Headers("Content-Type: application/json")
    @POST("/bookSearch/searchBook")
    Call<ResultObject<BookEntity>> searchBook(@Query("searchPage") String searchPage,
                                              @Query("searchContent") String searchContent);

    /**
     * 按照作者 Id 搜素对应的书籍信息
     * @param searchPage ： 查询的 SearchPage 对象
     * @param authorId ： 查询的作者 Id 对象
     * @return ： 得到的搜索结果对象
     */
    @Headers("Content-Type: application/json")
    @POST("/bookSearch/searchBookByAuthorId/{authorId}")
    Call<ResultObject<AuthorSimpleBook>> searchBookByAuthorId(@Body SearchPage searchPage,
                                                              @Path("authorId") Long authorId);
}
