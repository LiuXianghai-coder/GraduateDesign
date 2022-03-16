package org.graduate.user_service.tools;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 发送信息的帮助工具类
 *
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 20:13
 * @Project : user_service
 */
public final class SendMessage {
    public static String sendMessage(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("URL error");
        }

        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            return document.toString();
        } catch (Exception e) {
            System.out.println("Get URL error.");
            return null;
        }
    }
}
