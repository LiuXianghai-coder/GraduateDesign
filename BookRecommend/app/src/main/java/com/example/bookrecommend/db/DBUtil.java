package com.example.bookrecommend.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_IS_UPDATE;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_LAST_LOGIN_TIME;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_MATURITY_TIME;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_RECORD_ID;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_EMAIL;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_HEAD_IMAGE;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_ID;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_NAME;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_PASSWORD;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_PHONE;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.COLUMN_USER_SEX;
import static com.example.bookrecommend.db.UserInfoTable.UserInfo.TABLE_NAME;

/**
 * @Author: Administrator
 * @Date: 2021/03/24 18:30
 * @Project: BookRecommend
 **/
public class DBUtil {
    /**
     * 获取最近登录的用户记录信息
     * @param context ： 当前访问数据库的上下文
     * @return ： 如果查找到相关的登录记录信息，则返回对应的用户信息记录对象
     */
    public static UserInfoEntity getRecentUserInfoRecord(Context context) {
        UserInfoDbHelper dbHelper = new UserInfoDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                COLUMN_RECORD_ID,
                COLUMN_USER_ID,
                COLUMN_USER_PHONE,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_SEX,
                COLUMN_USER_HEAD_IMAGE,
                COLUMN_USER_PASSWORD,
                COLUMN_LAST_LOGIN_TIME,
                COLUMN_MATURITY_TIME,
                COLUMN_IS_UPDATE
        };

        String sortOrder = COLUMN_RECORD_ID + " DESC";
        @SuppressLint("Recycle") Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                "1"
        );

        UserInfoEntity obj = new UserInfoEntity();
        if (cursor.moveToNext()) {
            obj.setRecordId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_RECORD_ID)));
            obj.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            obj.setUserPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
            obj.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            obj.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            obj.setUserSex(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_SEX)));
            obj.setHeadImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_HEAD_IMAGE)));
            obj.setUserPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
            obj.setLastLoginTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN_TIME)));
            obj.setMaturityTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MATURITY_TIME)));
            obj.setIsUpdate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_IS_UPDATE)));
        }

        cursor.close();
        dbHelper.close();

        return obj;
    }

    /**
     * 保存用户信息对象
     * @param context ： 当前访问数据库的上下文对象
     * @param userInfo ： 待保存用户信息对象
     */
    public static void saveUserInfo(Context context, UserInfoEntity userInfo) {
        UserInfoDbHelper dbHelper = new UserInfoDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(TABLE_NAME, null, getContentValuesByUserInfo(userInfo));

        dbHelper.close();
    }

    /**
     * 更新传入的用户信息对象记录
     * @param context : 当前访问数据库的上下文
     * @param userInfo ： 待更新的用户信息对象
     */
    public static void updateUserInfo(Context context, UserInfoEntity userInfo) {
        UserInfoDbHelper dbHelper = new UserInfoDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = COLUMN_RECORD_ID + " = ?";
        String[] selectArgs = {String.valueOf(userInfo.getRecordId())};

        db.update(TABLE_NAME, getContentValuesByUserInfo(userInfo), selection, selectArgs);
    }

    private static ContentValues getContentValuesByUserInfo(UserInfoEntity userInfo) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_RECORD_ID, userInfo.getRecordId() + 1);
        values.put(COLUMN_USER_ID, userInfo.getUserId());
        values.put(COLUMN_USER_PHONE, userInfo.getUserPhone());
        values.put(COLUMN_USER_EMAIL, userInfo.getUserEmail());
        values.put(COLUMN_USER_NAME, userInfo.getUserName());
        values.put(COLUMN_USER_SEX, userInfo.getUserSex());
        values.put(COLUMN_USER_HEAD_IMAGE, userInfo.getHeadImage());
        values.put(COLUMN_USER_PASSWORD, userInfo.getUserPassword());
        values.put(COLUMN_LAST_LOGIN_TIME, userInfo.getLastLoginTime());
        values.put(COLUMN_MATURITY_TIME, userInfo.getMaturityTime());
        values.put(COLUMN_IS_UPDATE, userInfo.getIsUpdate());

        return values;
    }
}
