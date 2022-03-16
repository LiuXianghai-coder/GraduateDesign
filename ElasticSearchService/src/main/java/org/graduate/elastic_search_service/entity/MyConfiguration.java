package org.graduate.elastic_search_service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 对应的配置主机的一些配置信息
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/7
 * Time: 下午6:53
 */
@Configuration
@ConfigurationProperties(prefix = "myconfiguration")
@Data
public class MyConfiguration {
    // 对应的作者文档的索引地址名称
    private String authorIndexName;

    // 对应的书籍的索引地址名称
    private String bookIndexName;
}
