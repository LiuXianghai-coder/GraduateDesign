package com.example.bookrecommend.sington;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Administrator
 * @Date: 2021/03/25 12:42
 * @Project: BookRecommend
 **/
public final class AccountCheckTool {
    private static final String phonePattern = "\\+?\\d{2}?\\s?\\d+";

    private static final String emailPattern = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";

    private static final Pattern phoneRegex = Pattern.compile(phonePattern);

    private static final Pattern emailRegex = Pattern.compile(emailPattern);

    /**
     * 验证用户输入的账户是否是有效的
     * @param account ： 用户输入的账户
     * @return ： 是否是有效的账户
     */
    public static Boolean isValidAccount(@NonNull String account) {
        Matcher matcher = phoneRegex.matcher(account);
        if (matcher.find()) return true;

        matcher = emailRegex.matcher(account);
        return matcher.find();
    }

    /**
     * 检测输入的账户是否是手机号
     * @param account ： 输入的待检测的手机号
     * @return ： 是否为手机号
     */
    public static Boolean isPhone(@NonNull String account) {
        return phoneRegex.matcher(account).find();
    }

    /**
     * 检测输入的账户是否是邮件地址
     * @param account ： 输入的账户信息
     * @return : 是否为电子邮件地址
     */
    public static Boolean isEmail(@NonNull String account) {
        return emailRegex.matcher(account).find();
    }
}
