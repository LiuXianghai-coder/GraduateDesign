package org.graduate.kafkaservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取 DOM 信息的信息传递对象
 *
 * @author : LiuXianghai on 2021/1/20
 * @Created : 2021/01/20 - 15:42
 * @Project : GetDataService
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ParseDomMessage {
    // 传递的信息内容
    @JsonProperty("message")
    private String message;

    // 是否开始获取对应 DOM
    @JsonProperty("startFlag")
    private Boolean isStartParse;

    // 是否已经获取了对应的 DOM
    @JsonProperty("finishedFlag")
    private Boolean isParseEnd;

    // 对应的主机名称或地址
    @JsonProperty("host")
    private String host;

    /*
        是否使用 http 协议进行访问，
        不同的协议需要不同的程序进行不同的处理
     */
    @JsonProperty("useHttp")
    private Boolean isHttp;

    /*
        计数标记， 每次发送的消息可能会被重新接受，
        因此这个属性的作用就是每次增加技术来避免可能会重复接受到同一消息的问题
     */
    @JsonProperty("count")
    private Long count;

    public static void main(String[] args) throws JsonProcessingException {
        ParseDomMessage obj = new ParseDomMessage();

        obj.setMessage("Please start to parse Dom");
        // 这个信息对象应当是需要开始解析 Dom 的信息
        obj.setIsStartParse(true);
        // 这个信息应当是没有被解析完成的
        obj.setIsParseEnd(false);
        obj.setIsHttp(false);
        obj.setCount(1 % Long.MAX_VALUE);
        obj.setHost("127.0.0.1");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        System.out.println(json);
        System.out.println();

        json = "{\\\"message\\\":\\\"Please start to parse Dom\\\",\\\"startFlag\\\":true," +
                "\\\"finishedFlag\\\":false,\\\"host\\\":\\\"127.0.0.1\\\",\\\"useHttp\\\":false," +
                "\\\"count\\\":1}";

        System.out.println(json.length());

        ParseDomMessage dom = mapper.readValue(json, ParseDomMessage.class);
        System.out.println(dom.toString());
    }
}
