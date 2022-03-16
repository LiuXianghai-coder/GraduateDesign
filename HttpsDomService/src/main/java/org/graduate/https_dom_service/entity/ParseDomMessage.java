package org.graduate.https_dom_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;

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

    /**
     * 将消息对象转变为 ParseDomMessage 对象的方法
     * @param message ： 待转变的消息对象
     * @return ： 转变后得到的 ParseDom 对象
     */
    public static ParseDomMessage getParseDomMessageFromMessage(Object message) {
        // 如果传入的消息为空对象， 则直接返回
        if (null == message) return null;

        // 如果得到的消息对象不是 ConsumerRecord 类， 则直接返回
        if (ConsumerRecord.class != message.getClass()) return null;

        // 将得到的信息对象转变为 ConsumerRecord 对象
        ConsumerRecord<Object, Object> record = (ConsumerRecord<Object, Object>) message;

        // 获取记录的值， 这里是对应的对象
        Object recordValue = record.value();

        // 如果不是我们的 ParseDomMessage 类， 则直接返回
        if (ParseDomMessage.class != recordValue.getClass()) return null;

        // 将值对象转变为 ParseDomMessage 对象
        return (ParseDomMessage) recordValue;
    }
}
