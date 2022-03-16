package org.graduate.elastic_search_service;

import org.elasticsearch.client.RestHighLevelClient;
import org.graduate.elastic_search_service.configuration.RestClientConfig;
import org.graduate.elastic_search_service.entity.ContentType;
import org.graduate.elastic_search_service.entity.SearchContent;
import org.graduate.elastic_search_service.entity.SearchPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/10
 * Time: 下午3:57
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {RestClientConfig.class}
)
public class SearchControllerTest {
    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    public static void main(String[] args) {
        SearchContent searchContent = new SearchContent();
        searchContent.setContent("book");
        searchContent.setType(ContentType.BookName);
    }
}
