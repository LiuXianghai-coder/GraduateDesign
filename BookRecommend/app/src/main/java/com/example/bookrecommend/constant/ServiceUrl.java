package com.example.bookrecommend.constant;

import static com.example.bookrecommend.constant.ConstVariable.*;

/**
 * 相关服务程序的访问基地址
 *
 * @author: LiuXianghai
 * @Time: 2021年2月21日14:14:17
 */
public final class ServiceUrl {
    // 搜索服务的基础访问地址
    public static final String SEARCH_SERVICE_URL = "http://" + HOST_NAME + ":9030";

    // 上传图片的基础访问地址
    public static final String SAVE_FILE_SERVICE_URL = "http://39.99.129.90:9050";

    // 上传共享动态的基础访问地址
    public static final String USER_SHARE_URL = "http://"+ HOST_NAME + ":9040";

    // 书籍服务的基础访问路径
    public static final String BOOK_SERVICE_URL = "http://" + HOST_NAME + ":8081";

    // 用户服务的基础访问路径
    public static final String USER_SERVICE_URL = "http://" + HOST_NAME + ":9070";
}
