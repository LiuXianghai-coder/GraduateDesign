package org.graduate.elastic_search_service.entity;

import lombok.Data;

/**
 * ElasticSearch 的集群主机
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/7
 * Time: 下午6:55
 */
@Data
public class Machine {
    // 主机地址
    private String host;

    // 主机端口号
    private Integer port;

    // 使用的协议前缀， 即 http 或 https
    private String scheme;
}
