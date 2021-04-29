package org.graduate.kafkaservice.entity;

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

    // 检测链接是否正常可用的正则表达式
    private String hrefRegex;

    /*
        使用的浏览器名称， 实在是想不到一种能够解决多个电商平台之间不同架构的数据爬取 :(
        这是一种最直接了当的方式， 通过浏览器直接运行每个电商的 html， 以及加载相关的 JS
        获取相应的数据， 这是我能够想到的唯一的一种可行的解决方案。
        但是这么做也是有代价的， 首先这就要求有相关的权限来运行这个程序，
        同时， 这也会增加运行的时间：主要是以下两个方面：
            1. 每个文档的传输需要时间
            2. 页面的加载完成需要时间， 这个时间设置为 1.5s/每个页面 是一个合适的值
        加载完对应的 JS 后， 再将整个文档传输到对应的页面进行解析， 能够得到加载完成之后的页面
     */
    private String browser;

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

    /*
        https 协议
     */
    private String securityHttpProtocol;

    // 匹配那些没有协议前缀的 URL
    private String urlRegex;
}
