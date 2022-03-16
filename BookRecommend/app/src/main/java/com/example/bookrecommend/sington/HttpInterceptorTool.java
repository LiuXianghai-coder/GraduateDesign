package com.example.bookrecommend.sington;

import okhttp3.logging.HttpLoggingInterceptor;

public enum HttpInterceptorTool {
    DEFAULT_INSTANCE;

    private final HttpLoggingInterceptor interceptor;

    HttpInterceptorTool() {
        this.interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BASIC);
    }

    public HttpLoggingInterceptor getInterceptor() {
        return interceptor;
    }
}
