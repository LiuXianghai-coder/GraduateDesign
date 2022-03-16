package com.example.bookrecommend.sington;

import com.example.bookrecommend.service.BookService;
import com.example.bookrecommend.service.SaveFileService;
import com.example.bookrecommend.service.SearchService;
import com.example.bookrecommend.service.UserService;
import com.example.bookrecommend.service.UserShareService;

/**
 * @Author: Administrator
 * @Date: 2021/03/25 14:10
 * @Project: BookRecommend
 **/
public final class ServiceSingle {
    /*
        以饿汉式的方式加载对应的 UserService 实例对象，保证单例模式
     */
    private static final UserService userService = RetrofitTool.USER_SERVICE_RETROFIT
            .getRetrofit().create(UserService.class);

    /*
        以饿汉式的方式加载对应的 UserService 实例对象，保证单例模式
     */
    private static final SaveFileService saveFileService =  RetrofitTool.SAVE_FILE_RETROFIT
            .getRetrofit().create(SaveFileService.class);

    /*
        以饿汉式的方式加载对应的 BookService Retrofit 实例对象，保证单例模式
     */
    private static final BookService bookService = RetrofitTool.BOOK_SERVICE_RETROFIT
            .getRetrofit().create(BookService.class);

    /*
        以饿汉式的方式加载对应的 SearchService Retrofit 实例对象，保证单例模式
     */
    private static final SearchService searchService = RetrofitTool.SEARCH_SERVICE_RETROFIT
            .getRetrofit().create(SearchService.class);

    /*
        以饿汉式的方式加载对应的 UserShareService Retrofit 实例对象，保证单例模式
     */
    private static final UserShareService userShareService = RetrofitTool.USER_SHARE_RETROFIT
            .getRetrofit().create(UserShareService.class);


    public static UserService getUserService() {
        return userService;
    }

    public static BookService getBookService() {
        return bookService;
    }

    public static SearchService getSearchService() {
        return searchService;
    }

    public static UserShareService getUserShareService() {
        return userShareService;
    }

    public static SaveFileService getSaveFileService() {
        return saveFileService;
    }
}
