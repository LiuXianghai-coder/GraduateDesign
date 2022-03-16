package org.graduate.user_service.controller;

import org.graduate.user_service.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.graduate.user_service.tools.SendMessage.sendMessage;

/**
 * 发送验证码的控制器
 *
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 20:10
 * @Project : user_service
 */
@RestController
@RequestMapping(path = "/verifyCode")
@CrossOrigin(origins = "*")
public class VerifyCodeController {
    private static final String basicPhoneUrl =
            "http://www.qunfaduanxin.vip/api/send?username=1900327840&password=E10ADC3949BA59ABBE56E057F20F883E" +
                    "&gwid=oookc081&mobile=%s&message=【IM及时通讯】您的IM稳定版的验证码是：%s，如非本人操作请忽略";

    private final MailService mailService;

    @Autowired
    public VerifyCodeController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 通过邮箱获取对应的邮箱验证码
     * @param mailAddress ： 发送验证码的邮箱目的地址
     * @return ： 得到的验证码
     */
    @GetMapping(path = "/mailCode")
    public String getMailCode(@RequestParam(name = "mailAddress") String mailAddress) {
        int randomNum = (int) (Math.random() * 1e6);
        try {
            mailService.sendSimpleMessage(mailAddress, "【验证码】",
                    String.format("【IM及时通讯】您的IM稳定版的验证码是：%s，如非本人操作请忽略", randomNum));
//            response.getWriter().write(String.valueOf(randomNum));
            return String.valueOf(randomNum);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 通过手机号夺取对应的手机验证码
     * @param phone ： 发送验证码的目的手机号
     * @return ： 得到的验证码信息
     */
    @GetMapping(path = "/phoneCode")
    public String getPhoneCode(@RequestParam(name = "phone") String phone) {
        /*
            随机验证码， 这里的验证码是 6 位
         */
        int randomNum = (int) (Math.random() * 1e6);
        try {
            String result = sendMessage(String.format(basicPhoneUrl, phone,randomNum));
            if (result != null) {
                // 使用正则表达式匹配发送短信的结果
                Pattern regexSuccess = Pattern.compile("(?i)success");
	System.out.println("Result: " + result);
                Matcher matcher = regexSuccess.matcher(result);
                if (matcher.find()) {
//                    response.getWriter().write(String.valueOf(randomNum));
                    return String.valueOf(randomNum);
                } else {
//                    response.getWriter().write("404");
                    return "";
                }
            } else {
//                response.getWriter().write("400");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
