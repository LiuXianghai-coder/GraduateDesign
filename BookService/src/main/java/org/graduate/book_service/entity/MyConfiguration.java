package org.graduate.book_service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义的一些配置信息， 如：使用的连接默认协议
 *
 * @author : LiuXianghai on 2020/12/30
 * @Created : 2020/12/30 - 22:28
 * @Project : GetDataService
 */
@Component
@Data
@ConfigurationProperties(prefix = "myconfiguration")
public class MyConfiguration {
    // 默认的 http 协议
    private String defaultHttpProtocol;

    // 检测连接是否符合要求的正则表达式
    private String httpPrefixRegex;
}
