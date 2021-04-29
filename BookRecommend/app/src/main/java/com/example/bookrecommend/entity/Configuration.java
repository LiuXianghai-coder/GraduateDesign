package com.example.bookrecommend.entity;

import com.example.bookrecommend.constant.PageOptions;
import com.example.bookrecommend.sington.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author : LiuXianghai
 * @Date : 2021/02/22 15:39
 * @Product :  BookRecommend
 */
public final class Configuration {
    /*
        按照单例模式的构造要求， 在此对象访问的得到对象的方法都是满足单例模式的；
        但是，这种实现并不保证序列化的安全性。
     */
    private volatile static ObjectMapper mapper = null;

    private volatile static SearchPage searchPage = null;

    private volatile static SearchContent searchContent = null;

    /*
        获取的一个 Jackson Json 转换对象
     */
    public static ObjectMapper jacksonJsonMapper() {
        if (null == mapper) {
            synchronized (Configuration.class) {
                if (null == mapper) mapper = new ObjectMapper();
            }
        }
        return mapper;
    }

    /*
        获得默认的 SearchPage 对象
     */
    public static SearchPage defaultSearchPage() {
        if (null == searchPage) {
            synchronized (Configuration.class) {
                if (null == searchPage) {
                    searchPage = new SearchPage();
                    searchPage.setSize(PageOptions.DEFAULT_PAGE_SIZE);
                    searchPage.setStartLocation(0);
                    searchPage.setSortCol(PageOptions.BOOK_ISBN_STRING);
                }
            }
        }

        return searchPage;
    }

    /*
        获得默认的 SearchContent 对象
     */
    public static SearchContent defaultSearchContent() {
        if (null == searchContent) {
            synchronized (Configuration.class) {
                if (null == searchContent) {
                    searchContent = new SearchContent();
                    searchContent.setType(ContentType.ISBN);
                    searchContent.setContent("978");
                }
            }
        }

        return searchContent;
    }

//    /*
//        获得一个默认的 OkHttp 对象
//     */
//    public static OkHttpClient defaultOkHttpClient() {
//        if (null == defaultOkHttpClient) {
//            synchronized (Configuration.class) {
//                if (null == defaultOkHttpClient) {
//                    HttpLoggingInterceptor interceptor =
//                            new HttpLoggingInterceptor();
//                    interceptor.level(HttpLoggingInterceptor.Level.BODY);
//
//                    defaultOkHttpClient = new OkHttpClient
//                            .Builder()
//                            .addInterceptor(interceptor)
//                            .build();
//                }
//            }
//        }
//
//        return defaultOkHttpClient;
//    }
//
//    /*
//        根据传入的 OkHttp 客户端对象构建一个 Retrofit 对象
//     */
//    public static Retrofit defaultRetrofit() {
//        if (null == defaultRetrofit) {
//            synchronized (Configuration.class) {
//                if (null == defaultRetrofit) {
//                    defaultRetrofit = new Retrofit.Builder()
//                            .baseUrl(ServiceUrl.SEARCH_SERVICE_URL)
//                            .addConverterFactory(JacksonConverterFactory.create())
//                            .client(defaultOkHttpClient())
//                            .build();
//                }
//            }
//        }
//
//        return defaultRetrofit;
//    }
//
//    /*
//        保存文件的访问 Retrofit 对象
//     */
//    public static Retrofit saveFileRetrofit() {
//        if (null == saveFileRetrofit) {
//            synchronized (Configuration.class) {
//                if (null == saveFileRetrofit) {
//                    saveFileRetrofit = new Retrofit.Builder()
//                            .baseUrl(ServiceUrl.SAVE_FILE_SERVICE_URL)
//                            .addConverterFactory(JacksonConverterFactory.create())
//                            .client(defaultOkHttpClient())
//                            .build();
//                }
//            }
//        }
//
//        return saveFileRetrofit;
//    }
}
