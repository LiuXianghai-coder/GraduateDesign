package com.example.bookrecommend.sington;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 密码处理工具类
 *
 * @Author: Administrator
 * @Date: 2021/03/25 13:09
 * @Project: BookRecommend
 **/
public final class PasswordTool {
    /**
     * 将输入的密码进行 SHA-512 加密
     * @param input ： 输入的待加密密码
     * @return ： 经过 SHA-512 算法加密后的密码
     */
    public static String SHA512(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

            byte[] md = messageDigest.digest(input.getBytes());

            BigInteger bigInteger = new BigInteger(1, md);
            StringBuilder hashText = new StringBuilder(bigInteger.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            return hashText.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
