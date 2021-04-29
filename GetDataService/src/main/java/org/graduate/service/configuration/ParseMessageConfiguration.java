package org.graduate.service.configuration;

import org.graduate.service.constants.InfoConstant;
import org.graduate.service.entity.ParseDomMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 传递的解析 DOM 信息的配置类
 * @author : LiuXianghai on 2021/1/20
 * @Created : 2021/01/20 - 15:45
 * @Project : GetDataService
 */
@Configuration
public class ParseMessageConfiguration {

    /**
     * 发送消息给Kafka， 请求对应的监听对象开始解析对应的 DOM
     * @return ： 一个新的 ParseDom 对象， 它应当是开始去解析， 但是没有完成解析的
     */
    @Bean
    public ParseDomMessage startParseDomMessage() {
        ParseDomMessage obj = new ParseDomMessage();

        obj.setMessage(InfoConstant.START_PARSE_DOM_MESSAGE);
        // 这个信息对象应当是需要开始解析 Dom 的信息
        obj.setIsStartParse(true);
        // 这个信息应当是没有被解析完成的
        obj.setIsParseEnd(false);

        return obj;
    }

    /**
     * 生成一个完成了 Dom 解析的 ParseDomMessage 对象
     * @return ： 一个已经完成解析 Dom任务的 ParseDomMessage 对象
     */
    @Bean
    public ParseDomMessage finishParseDomMessage() {
        ParseDomMessage obj = new ParseDomMessage();

        obj.setMessage(InfoConstant.FINISHED_DOM_PARSE_MESSAGE);
        // 它不是需要开始解析 Dom 的信息
        obj.setIsStartParse(false);
        // 已经结束解析 Dom
        obj.setIsParseEnd(true);

        return obj;
    }
}
