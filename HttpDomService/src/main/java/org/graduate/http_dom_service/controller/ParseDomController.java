package org.graduate.http_dom_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.graduate.http_dom_service.component.MyConfiguration;
import org.graduate.http_dom_service.component.MyData;
import org.graduate.http_dom_service.configuration.JsonObjectConfiguration;
import org.graduate.http_dom_service.configuration.ParseMessageConfiguration;
import org.graduate.http_dom_service.entity.DomObject;
import org.graduate.http_dom_service.entity.ParseDom;
import org.graduate.http_dom_service.entity.ParseDomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static org.graduate.http_dom_service.tools.FileHelper.writeToFile;


/**
 * 解析 DOM 文档的控制器
 *
 * @author : LiuXianghai on 2021/1/4
 * @Created : 2021/01/04 - 20:05
 * @Project : GetDataService
 */
@Slf4j
@RestController
@RequestMapping(path = "/parseData")
public class ParseDomController {
    // 数据信息配置对象
    private final MyData myData;

    // 日志记录信息
    private final Logger logger = LoggerFactory.getLogger(ParseDomController.class);

    // 产生 DOM 文档对象的工具类
    private final ParseDom parseDom;

    private final ParseMessageConfiguration messageConfiguration;

    // Kafka 消息发送模板
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    // Json 解析配置类， 获取对应的处理 json 的Bean
    private final JsonObjectConfiguration jsonObjectConfiguration;

    // 计数器， 避免收到的是同一个信息
    public static Long count = 1L;

    @Autowired
    public ParseDomController(MyConfiguration myConfiguration,
                              MyData myData,
                              ParseMessageConfiguration messageConfiguration,
                              KafkaTemplate<Object, Object> kafkaTemplate,
                              JsonObjectConfiguration jsonObjectConfiguration) {
        // 自定义配置属性类， 通过读取 application.yml 配置文件获取
        this.myData                 =   myData;
        this.parseDom               =   new ParseDom(myConfiguration);
        this.messageConfiguration   =   messageConfiguration;
        this.kafkaTemplate = kafkaTemplate;
        this.jsonObjectConfiguration = jsonObjectConfiguration;
    }

    /**
     * 将传入的字符串内容转换为对应的 DOM 对象，也就是o
     * 说， 这里是嵌入 Js 脚本的目的地址
     * @param request ： 请求的信息对象
     * @param dom ： 生成的 DOM 对象的内容
     * @return ： 生成的 DOM 文档对象
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/genDom", consumes = "application/json")
    public HttpStatus genDom(HttpServletRequest request, @RequestBody DomObject dom)
            throws JsonProcessingException {
        String requestRemoteArr = request.getRemoteAddr();
        logger.info(logger.getName(), " request address: " + requestRemoteArr);

        if (!parseDom.isTrustable(requestRemoteArr)) return HttpStatus.BAD_REQUEST;

        // 将得到的 Dom 文档写入到对应的 html 文件内
        writeToFile(new File(myData.getPath() + myData.getElementsFileName()), dom.getDom());

        // Json 处理对象
        ObjectMapper mapper = jsonObjectConfiguration.jacksonMapper();

        // 处理成功后， 使用 Kafka 发送对应的信息
        ParseDomMessage obj = messageConfiguration.finishParseDomMessage();
        obj.setHost(requestRemoteArr);
        // 设置 count 避免消息接受者收到同一个消息
        /*
            尽管可能性不大， 但是一旦值溢出则必须再次复位，
            此时的消息应当已经是被清理过的，所以不会导致收到同一个消息
         */
        if (Long.MAX_VALUE == count) count = 1L;
        obj.setCount(count++);
        ListenableFuture<SendResult<Object, Object>> future =
                kafkaTemplate.sendDefault(mapper.writeValueAsString(obj));
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info(throwable.toString());
            }

            @Override
            public void onSuccess(SendResult<Object, Object> objectObjectSendResult) {
                // 将发送的消息输出到日志
                logger.info(logger.getName() + " write to elements success.");
            }
        });

        return HttpStatus.OK;
    }
}
