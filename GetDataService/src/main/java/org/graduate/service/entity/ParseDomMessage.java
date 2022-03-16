package org.graduate.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

/**
 * 获取 DOM 信息的信息传递对象
 *
 * @author : LiuXianghai on 2021/1/20
 * @Created : 2021/01/20 - 15:42
 * @Project : GetDataService
 */
@Data
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
}
