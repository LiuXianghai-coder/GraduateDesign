package com.example.bookrecommend.constant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bookrecommend.R;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.AuthorSimple;
import com.example.bookrecommend.pojo.BookEntity;
import com.example.bookrecommend.pojo.UserInfo;
import com.example.bookrecommend.sington.SingleObject;
import com.example.bookrecommend.sington.TimeTools;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : LiuXianghai
 * @Date : 2021/02/25 10:23
 * @Product :  BookRecommend
 */
public final class Tools {
    private final static String isbRegex = "\\d+";

    private final static Pattern isbnPattern = Pattern.compile(isbRegex);

    private final static CharSequence[] options = {"拍照获取", "从已有照片中获取", "取消"};

    /**
     * 检测传入的地址是否符合规范
     *
     * @param url : 待检测的 URL
     * @return ： 是否为标准的 URL
     */
    public static Boolean isUrl(String url) {
        if (null == url || 0 == url.trim().length()) return false;
        Pattern urlPattern = Pattern.compile("http[s]?://[^.]+\\..+");
        Matcher matcher = urlPattern.matcher(url);

        return matcher.find();
    }

    /**
     * 设置发送验证码的时间间隔
     *
     * @param verifyCodeButton ： 待设置的按钮对象
     */
    @SuppressLint("SetTextI18n")
    public static void setStopGetVerifyCode(@NonNull Context context,
                                            @NonNull Button verifyCodeButton) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                verifyCodeButton.setClickable(msg.getData().getBoolean("clickAble"));
                verifyCodeButton.setText(msg.getData().getString("text"));
//                Log.d("buttonVal", msg.getData().getString("text"));
                verifyCodeButton.setTextSize(Float.parseFloat(msg.getData().getString("textSize")));
                super.handleMessage(msg);
            }
        };

        new Thread(() -> {
            int remainTime = ConstVariable.VERIFY_CODE_INTERNAL_TIME;
            while (remainTime >= 0) {
                Bundle bundle = new Bundle();
                bundle.putString("text", remainTime + "s后重新发送");
                bundle.putString("textSize", String.valueOf(ConstVariable.VERIFY_CODE_INTERVAL_TEXT_SIZE));
                bundle.putBoolean("clickAble", false);

                Message msg = new Message();
                msg.setData(bundle);
                remainTime--;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Bundle bundle = new Bundle();
            bundle.putString("text", context.getString(R.string.get_code_string));
            bundle.putString("textSize", String.valueOf(ConstVariable.VERIFY_CODE_TEXT_SIZE));
            bundle.putBoolean("clickAble", true);

            Message msg = new Message();
            msg.setData(bundle);

            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 将 UserInfoEntity 对象转变为对应的 UserInfo 对象
     *
     * @param obj ： 待转换的 UserInfoEntity 对象
     * @return ： 转换后得到的 UserInfo 对象
     */
    public static UserInfo convertToUserInfo(@NonNull UserInfoEntity obj) {
        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(obj.getUserId());
        userInfo.setUserSex(obj.getUserSex());
        userInfo.setUserPhone(obj.getUserPhone());
        userInfo.setUserEmail(obj.getUserEmail());
        userInfo.setUserName(obj.getUserName());
        userInfo.setHeadImage(obj.getHeadImage());
        userInfo.setHoldingFeatures(obj.getFeatures());
        userInfo.setUserPassword(obj.getUserPassword());

        return userInfo;
    }

    /**
     * 将一个对应的 UserInfo 对象转变为对应的 UserInfoEntity 对象，
     * 这个转变后的 UserInfoEntity 对象有默认的上次登陆时间和预计到期时间
     *
     * @param obj ： 待转换的 UserInfo 对象
     * @return ： 转换后的 UserInfoEntity 对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static UserInfoEntity convertToUserInfoEntity(@NonNull UserInfo obj) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();

        userInfoEntity.setUserId(obj.getUserId());
        userInfoEntity.setUserPhone(obj.getUserPhone());
        userInfoEntity.setUserEmail(obj.getUserEmail());
        userInfoEntity.setUserSex(obj.getUserSex());
        userInfoEntity.setUserPassword(obj.getUserPassword());
        userInfoEntity.setHeadImage(obj.getHeadImage());
        userInfoEntity.setUserName(obj.getUserName());
        userInfoEntity.setIsUpdate((short) 0);
        userInfoEntity.setLastLoginTime(TimeTools.currentDateTimeString());
        userInfoEntity.setMaturityTime(TimeTools.maturityTimeString());
        userInfoEntity.setFeatures(obj.getHoldingFeatures());

        return userInfoEntity;
    }

    // 更新对应的 UserInfoEntitySingle 单例对象，这是为了使得在登录后每个界面都可以访问
    public static void updateUserInfoEntitySingle(@NonNull UserInfoEntity userInfoEntity) {
        Log.d("Update", userInfoEntity.toString());

        UserInfoEntity infoEntity = SingleObject.getUserInfoEntity();
        infoEntity.setRecordId(userInfoEntity.getRecordId());
        infoEntity.setUserId(userInfoEntity.getUserId());
        infoEntity.setUserPhone(userInfoEntity.getUserPhone());
        infoEntity.setUserEmail(userInfoEntity.getUserEmail());
        infoEntity.setUserName(userInfoEntity.getUserName());
        infoEntity.setUserSex(userInfoEntity.getUserSex());
        infoEntity.setHeadImage(userInfoEntity.getHeadImage());
        infoEntity.setUserPassword(userInfoEntity.getUserPassword());
        infoEntity.setLastLoginTime(userInfoEntity.getLastLoginTime());
        infoEntity.setMaturityTime(userInfoEntity.getMaturityTime());
        infoEntity.setIsUpdate(userInfoEntity.getIsUpdate());
        infoEntity.setFeatures(userInfoEntity.getFeatures());
    }

    /*
        通过对应的 BookEntity 对象来设置对应的作者显示字符串
     */
    public static void setAuthorsString(@NonNull BookEntity bookEntity) {
        // 使用 StringBuilder 来存储作者姓名组合的字符串，这是为了效率 :）
        StringBuilder authorsString = new StringBuilder();
        // 使用一个额外的 List 来保存当前书籍的作者列表， 避免多次访问
        List<AuthorSimple> authorSimples = bookEntity.getAuthors();
        for (AuthorSimple authorSimple: authorSimples) {
            authorsString.append(authorSimple.getAuthorName()).append(" "); // 多个作者使用 Tab 分割符分割
        }

        bookEntity.setAuthorsString(authorsString.toString());
    }

    public static boolean isIsbn(@NonNull String content) {
        Matcher matcher = isbnPattern.matcher(content);

        return matcher.find();
    }
}
