package com.example.bookrecommend.db;

import android.provider.BaseColumns;

/**
 * 用户信息的本地数据表
 *
 * @Author: Administrator
 * @Date: 2021/03/23 13:02
 * @Project: BookRecommend
 **/
public final class UserInfoTable {
    // 创建用户信息记录表的 SQL 语句
    public static final String SQL_CREATE_DATABASE = "CREATE TABLE " + UserInfo.TABLE_NAME + "(" +
            UserInfo.COLUMN_RECORD_ID + " BIGINT PRIMARY KEY, " +
            UserInfo.COLUMN_USER_ID + " BIGINT, " +
            UserInfo.COLUMN_USER_PHONE + " VARCHAR(16) DEFAULT '', " +
            UserInfo.COLUMN_USER_EMAIL + " VARCHAR(255) DEFAULT ''," +
            UserInfo.COLUMN_USER_NAME + " VARCHAR(30) NOT NULL, " +
            UserInfo.COLUMN_USER_SEX + " VARCHAR(4) NOT NULL, " +
            UserInfo.COLUMN_USER_HEAD_IMAGE + " TEXT DEFAULT ''," +
            UserInfo.COLUMN_USER_PASSWORD + " CHAR(128) NOT NULL, " +
            UserInfo.COLUMN_LAST_LOGIN_TIME + " TEXT NOT NULL, " +
            UserInfo.COLUMN_MATURITY_TIME + " TEXT NOT NULL, " +
            UserInfo.COLUMN_IS_UPDATE + " SHORT DEFAULT FALSE);";

    // 删除本地数据的所有记录
    public static final String SQL_DELETE_DATABASE = "DROP TABLE " + UserInfo.TABLE_NAME;

    public static class UserInfo implements BaseColumns {
        /*
            当前的表名，与远程数据库表对应，以便于分析和使用
         */
        public static final String TABLE_NAME = "user_info";

        /*
            用户表的记录信息，用于记录用户的登录记录
         */
        public static final String COLUMN_RECORD_ID = "record_id";

        /*
            用户 ID 列
         */
        public static final String COLUMN_USER_ID = "user_id";

        /*
            用户手机号列， 这是注册和登录时的一个手段
         */
        public static final String COLUMN_USER_PHONE = "user_phone";

        /*
            用户邮箱地址，也是登录时的一种方式
         */
        public static final String COLUMN_USER_EMAIL = "user_email";

        /*
            用户当前保存的用户名
         */
        public static final String COLUMN_USER_NAME = "user_name";

        /*
            用户性别列
         */
        public static final String COLUMN_USER_SEX = "user_sex";

        /*
            用户头像地址路径
         */
        public static final String COLUMN_USER_HEAD_IMAGE = "head_image";

        /*
            用户的登录密码， 这是经过加密之后的密码，
            为了减少每次登录时都需要访问服务器， 因此保存在本地中以便于直接登录
         */
        public static final String COLUMN_USER_PASSWORD = "user_password";

        /*
            用户上次登录的时间， 用于检测当前的登录状态是否过期， 貌似 SQLite 没有时间类型。。。。。。
         */
        public static final String COLUMN_LAST_LOGIN_TIME = "last_login_time";

        /*
            预计到期时间列
         */
        public static final String COLUMN_MATURITY_TIME = "maturity_time";

        /*
            用户信息是否被修改过， 如果被修改过，
            那么不管登录状态是否到期， 都需要重新进行登录
         */
        public static final String COLUMN_IS_UPDATE = "is_update";
    }
}
