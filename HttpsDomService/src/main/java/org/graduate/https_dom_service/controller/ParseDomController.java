package org.graduate.https_dom_service.controller;

import org.graduate.https_dom_service.configuration.ParseMessageConfiguration;
import org.graduate.https_dom_service.entity.DomObject;
import org.graduate.https_dom_service.entity.MyConfiguration;
import org.graduate.https_dom_service.entity.MyData;
import org.graduate.https_dom_service.entity.ParseDomMessage;
import org.graduate.https_dom_service.tools.ParseDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static org.graduate.https_dom_service.tools.FileHelper.writeToFile;


/**
 * 解析 DOM 文档的控制器
 *
 * @author : LiuXianghai on 2021/1/4
 * @Created : 2021/01/04 - 20:05
 * @Project : HttpsDomService
 */
@RestController
@RequestMapping(path = "/parseData")
public class ParseDomController {
    // 数据信息配置对象
    private final MyData myData;

    // 日志记录信息
    private final Logger logger = LoggerFactory.getLogger(ParseDomController.class);

    // 产生 DOM 文档对象的工具类
    private final ParseDom parseDom;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private final ParseMessageConfiguration messageConfiguration;

    private Long count = 0L;

    @Autowired
    public ParseDomController(MyConfiguration myConfiguration,
                              MyData myData,
                              KafkaTemplate<Object, Object> kafkaTemplate,
                              ParseMessageConfiguration messageConfiguration) {
        // 自定义配置属性类， 通过读取 application.yml 配置文件获取
        this.myData                 =   myData;
        this.parseDom               =   new ParseDom(myConfiguration);
        this.kafkaTemplate          =   kafkaTemplate;
        this.messageConfiguration   =   messageConfiguration;
    }

    /**
     * 将传入的字符串内容转换为对应的 DOM 对象
     * @param request ： 请求的信息对象
     * @param dom ： 生成的 DOM 对象的内容
     * @return ： 生成的 DOM 文档对象
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/genDom", consumes = "application/json")
    public HttpStatus genDom(HttpServletRequest request, @RequestBody DomObject dom){
//        logger.debug(logger.getName() + " get Dom String: " + dom + "\n");

        String requestRemoteArr = request.getRemoteAddr();
        logger.info(logger.getName() + " request address: " + requestRemoteArr);

        if (!parseDom.isTrustable(requestRemoteArr)) return HttpStatus.BAD_REQUEST;

        // 将得到的 Dom 文档写入到对应的 html 文件内
        writeToFile(new File(myData.getPath() + myData.getHtmlFileName()), dom.getDom());

        ParseDomMessage message = messageConfiguration.finishParseDomMessage();
        message.setCount(++count % Long.MAX_VALUE);

        // 处理成功后， 使用 Kafka 发送对应的信息
        kafkaTemplate.sendDefault(message);

        return HttpStatus.OK;
    }
}
