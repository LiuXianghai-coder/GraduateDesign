package com.example.bookrecommend.sington;

import com.example.bookrecommend.constant.ServiceUrl;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 一些 Retrofit 网络访问工具，
 * 这是为了保证每个工具在这个上下文中都只会存在一个对应实例
 */
public enum RetrofitTool {
    // 保存文件（这里特指图片）的 Retrofit 工具
    SAVE_FILE_RETROFIT(ServiceUrl.SAVE_FILE_SERVICE_URL),

    // 搜索服务的 Retrofit 工具， 主要对应于书籍的搜索和动态的初始展示界面
    SEARCH_SERVICE_RETROFIT(ServiceUrl.SEARCH_SERVICE_URL),

    // 书籍的基本服务信息 Retrofit 工具， 包括书评信息、评论信息等
    BOOK_SERVICE_RETROFIT(ServiceUrl.BOOK_SERVICE_URL),

    // 动态共享的基本服务信息，包括添加评论、点赞等
    USER_SHARE_RETROFIT(ServiceUrl.USER_SHARE_URL),

    // 用户的基本服务，包括登录，注册等
    USER_SERVICE_RETROFIT(ServiceUrl.USER_SERVICE_URL);

    private final Retrofit retrofit;

    RetrofitTool(String accessUrl) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(accessUrl)
                .client(HttpClientTool.DEFAULT_INSTANCE.getClient())
                .addConverterFactory(JacksonConverterFactory
                        .create(SingleObjectMapper
                                .DEFAULT_INSTANCE.getMapper()
                        ))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
