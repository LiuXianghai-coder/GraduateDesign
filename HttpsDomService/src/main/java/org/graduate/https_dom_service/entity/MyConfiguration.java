package org.graduate.https_dom_service.entity;

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
    /*
        值得信赖的访问 IP 地址， 这是为了避免某些恶意访问
     */
    private List<String> trustableRemoteAddr;

    /*
        执行文件的前缀内容
     */
    private String fileProtocolPrefix;

    /*
        执行文件路径的检测正则表达式
     */
    private String fileProtocolRegex;
}
