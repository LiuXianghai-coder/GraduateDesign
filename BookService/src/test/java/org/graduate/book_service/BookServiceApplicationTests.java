package org.graduate.book_service;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.graduate.book_service.tools.TimeTools;
import org.graduate.book_service.data.UserBookComment;
import org.graduate.book_service.data.UserBookMark;
import org.graduate.book_service.repository.BookRepository;
import org.graduate.book_service.repository.UserBookCommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BookServiceApplicationTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookCommentRepo userBookCommentRepo;

    public static String doHanLPApi(String token, String url, Map<String, Object> params) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //添加header请求头，token请放在header里
            httpPost.setHeader("token", token);
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
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws JsonProcessingException {
        //请求头中的token   需要进行替换成在官网申请的新token
        String token="14492b4a3254400d9498f0af76cd91631618025478099token";
        //申请的接口地址
        String url="http://comdo.hanlp.com/hanlp/v1/textAnalysis/sentimentAnalysis";
        //处理分析的文本，作为params参数传入  support@hanlp.com
        Map<String,Object> params= new HashMap<>();

        // 参数传入要处理分析的文本
        params.put("text","这本书写的太好了!");

//        System.out.println(doHanLPApi(token, url, params));

//        //执行HanLP接口，result为返回的结果
//        String result = doHanLPApi(token,url,params);
//        //打印输出结果
//        System.out.println("result: " + result);

        UserBookMark mark = new UserBookMark();
        mark.setUserId(2L);
        mark.setIsbn(123456);
        mark.setScore((short) 0);
        mark.setComment("");
        mark.setMarkDate(TimeTools.currentTime());

        ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        System.out.println(mapper.writeValueAsString(mark));

        UserBookComment obj = mapper
                .readValue("{\"userId\":0,\"isbn\":9787115553201,\"commentId\":1,\"commentContent\":\"这本书写的太好了\",\"commentDate\":\"2021-04-28T10:41:05.660123+08:00\",\"userInfo\":{\"userId\":0,\"userName\":\"刘湘海\",\"userSex\":\"男\"," +
                        "\"headImage\":\"http://39.99.129.90:9050/file/JPEG_20210427_194052_840093277.jpg?userId=0\"}}",
                        UserBookComment.class);

        System.out.println(obj.toString());

        System.out.println("OffsetDateTime: " + OffsetDateTime.now());
    }
}
