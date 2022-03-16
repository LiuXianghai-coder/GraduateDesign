package org.graduate.book_service.tools;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.graduate.book_service.constant.Const;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/13
 * Time: 上午10:52
 */

@Async
public class SegmentJudgeTools {
    //请求头中的token   需要进行替换成在官网申请的新token
    private final static String token = "14492b4a3254400d9498f0af76cd91631618025478099token";
    //申请的接口地址
    private final static String url = "http://comdo.hanlp.com/hanlp/v1/textAnalysis/sentimentAnalysis";

    private final static Pattern positiveRegex =
            Pattern.compile(Const.POSITIVE_RESULT_REGEX);

    private static String doHanLPApi(Map<String, Object> params) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(SegmentJudgeTools.url);
            //添加header请求头，token请放在header里
            httpPost.setHeader("token", SegmentJudgeTools.token);
            // 创建参数列表
            List<NameValuePair> paramList = new ArrayList<>();
            if (params != null) {
                for (String key : params.keySet()) {
                    //所有参数依次放在paramList中
                    paramList.add(new BasicNameValuePair(key, (String) params.get(key)));
                }
                //模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean isPositive(@NonNull String text) {
        Map<String,Object> params = new HashMap<>();
        params.put("text", text);

        String result = doHanLPApi(params);

        assert result != null;
        Matcher matcher = positiveRegex.matcher(result);

        return matcher.find();
    }
}
