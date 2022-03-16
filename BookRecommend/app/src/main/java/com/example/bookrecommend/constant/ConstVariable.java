package com.example.bookrecommend.constant;

/**
 * 定义的一些基本常量信息
 *
 * @Author : LiuXianghai
 * @Date : 2021/03/04 21:31
 * @Product :  BookRecommend
 */
public final class ConstVariable {
    // 为了与对应的请求参数对应起来，这个为需要的参数名称
    public final static String IMAGE_FILE_STRING = "imageFile";

    // 主机地址
    public final static String HOST_NAME = "192.168.43.132";

    // 再次获取验证码的间隔时间
    public final static int VERIFY_CODE_INTERNAL_TIME = 60;

    // 初始验证码的字体大小
    public final static float VERIFY_CODE_TEXT_SIZE = 20.0f;

    // 等待再次发送验证码时的字体大小
    public final static float VERIFY_CODE_INTERVAL_TEXT_SIZE = 11.0f;

    // 性别为男的字符串表示形式
    public final static String MALE_GENDER = "男";

    // 性别为女的字符串表示形式
    public final static String FEMALE_GENDER = "女";

    /**
     * 书评信息的基础加载页面，每次加载时都需要在最后位置添加对应的书评 id 以构成完整的加载路径
     */
    public final static String BOOK_REVIEW_BASIC_URL = "http://" + HOST_NAME + ":8081/view/bookReview/";

    /**
     * 用户动态信息的详细信息，需要在最后加上相关的 shareId 来获取相关的详情信息
     */
    public final static String SHARE_DETAIL_BASIC_URL = "http://" + HOST_NAME + ":9040/shareContent/content/";
}
