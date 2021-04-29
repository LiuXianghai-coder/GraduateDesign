package org.graduate.elastic_search_service.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 访问 ElasticSearch 的配置类
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/8
 * Time: 上午10:13
 */
@Configuration
public class RestClientConfig {
    /*
        RestLowLevel Client 对象
     */
    @Bean(name = "restClient")
    public RestClient restClient() {
        return RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http")
        ).build();
    }

    /*
        RestHighLevel Client 对象
     */
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")
                )
        );
    }
}
