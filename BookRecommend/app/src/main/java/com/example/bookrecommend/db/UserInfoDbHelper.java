package com.example.bookrecommend.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.bookrecommend.db.UserInfoTable.*;

/**
 * 本地 SQLite 数据库的创建和删除操作
 *
 * @Author: Administrator
 * @Date: 2021/03/23 13:17
 * @Project: BookRecommend
 **/
public class UserInfoDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserInfo.db";

    public UserInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DATABASE);
        db.execSQL(SQL_CREATE_DATABASE);
    }
}
