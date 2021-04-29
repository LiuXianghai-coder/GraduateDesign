package com.example.bookrecommend.constant;

import static com.example.bookrecommend.constant.ConstVariable.*;

/**
 * @Author: Administrator
 * @Date: 9:05
 * @Project: BookRecommend
 **/
public class WebViewUrl {
    /**
     * 需要得到对应的书评 ID 获取对应的 WebView
     * 原型如下：https://192.168.100.28:8080/view/bookReview/{reviewId}
     */
    public final static String BOOK_REVIEW_WEB_VIEW_URL = "https://" + HOST_NAME + ":8080/view/bookReview/";

    /**
     * 得到对应的用户动态的 WebView 内容
     * 原型如下：https://192.168.100.28:9040/shareContent/content/{shareId}/{userId}
     */
    public final static String BOOK_SHARE_CONTENT_WEB_VIEW = "http://" + HOST_NAME + ":9040/";
}
