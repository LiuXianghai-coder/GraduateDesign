package org.graduate.kafkaservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.graduate.kafkaservice.entity.MyConfiguration;
import org.graduate.kafkaservice.entity.ParseDomMessage;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/30
 * Time: 下午12:05
 */
@Slf4j
@RestController
@RequestMapping(path = "/kafkaMessage")
@EnableKafka
public class KafkaServiceController {
    private final MyConfiguration myConfiguration;

    private final Map<String, String> headUrlMap;

    private Boolean isFinished = true;

    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaServiceController(MyConfiguration myConfiguration) {
        this.myConfiguration = myConfiguration;

        this.headUrlMap = new HashMap<>();
        headUrlMap.put("http", "http://127.0.0.1:8083/data");
        headUrlMap.put("https", "https://127.0.0.1:8082/data");
    }

    @GetMapping(path = "")
    public Integer processStatus() {
        return isFinished ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
    }

    @KafkaListener(topics = "graduate", groupId = "test-consumer-group")
    public void handle(ConsumerRecord<Object, Object> record)
            throws JsonProcessingException {
        ParseDomMessage message  = mapper.readValue((String) record.value(),
                ParseDomMessage.class);

        log.info("Get Message: " + message);

        /*
            开始解析 Dom
         */
        if (message.getIsStartParse()) {
            if (!isFinished) return;

            log.info("Start parse Dom in browser");
            this.isFinished = false;
            startParse(message);
            return;
        }

        /*
            解析已经结束， 设置对应的属性
         */
        if (message.getIsParseEnd()) {
            log.info("Parse Dom has been finished.");
            this.isFinished = true;
        }
    }

    private void startParse(@NotNull ParseDomMessage message) {
        if (message.getIsHttp()) {
            runHtmlOnBrowser("http");
            return;
        }

        runHtmlOnBrowser("https");
    }

    /**
     * 将对应的 Html 文档在浏览器中执行以得到完整的 Dom 数据
     */
    public void runHtmlOnBrowser(String head) {
        // 得到执行待 Html 文件的浏览器程序
        String browserName  =   myConfiguration.getBrowser();

        log.info("browser name: " + browserName);

        // 解析的 URL 地址
        String url      =   headUrlMap.get(head);

        // 整合相对应的执行命令
        String command      =   combineCommand(
                new String[]{browserName, url}, " ");
        try {
            // 通过浏览器执行 Html文档
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 整合所有的命令为一个整体命令
     * @param commands ： 待整合的命令列表
     * @param splitChar : 分隔符
     * @return ： 整合后的命令结果
     */
    public String combineCommand(String[] commands, String splitChar) {
        if (null == commands || 0 == commands.length) return null;

        StringBuilder command = new StringBuilder();
        // 默认以空白字符作为分隔符
        for (String string: commands) {
            command.append(string).append(splitChar);
        }
        // 移除最后一个分隔符
        command.deleteCharAt(command.length() - 1);

        return command.toString();
    }
}
