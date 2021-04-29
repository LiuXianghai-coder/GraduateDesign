package org.graduate.user_service.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 20:17
 * @Project : user_service
 */
@Service
public interface MailService {
    /**
     * 发送简单消息至指定的对象
     * @param to ： 目的地址
     * @param subject ： 邮件主体
     * @param text ： 正文内容
     */
    void sendSimpleMessage(String to, String subject, String text);

    /**
     * 发送消息至指定的邮箱地址， 携带附件
     * @param to ： 发送邮件的目的地址
     * @param subject ： 邮件主体
     * @param text ： 正文内容
     * @param pathToAttachment ： 附件内容
     */
    void sendMessageWithAttachment(String to, String subject,
                                   String text, String pathToAttachment);

    /**
     * 发送消息使用指定的模板
     * @param to ： 发送邮件的目的地址
     * @param subject ：邮件主题
     * @param templateModel ： 模板内容
     */
    void sendMessageUsingThymeleafTemplate(String to, String subject,
                                           Map<String, Object> templateModel)
            throws IOException, MessagingException;
}
