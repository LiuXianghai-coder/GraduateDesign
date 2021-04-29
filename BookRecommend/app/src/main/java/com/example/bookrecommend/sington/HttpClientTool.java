package com.example.bookrecommend.sington;

import okhttp3.OkHttpClient;

/**
 * 一些 HttpClient 来进行互联网的访问， 使用枚举类型时为了保证单例模式
 */
public enum HttpClientTool {
    DEFAULT_INSTANCE(HttpInterceptorTool.DEFAULT_INSTANCE);

    private final OkHttpClient client;

    HttpClientTool(HttpInterceptorTool interceptorTool) {
        client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptorTool.getInterceptor())
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
