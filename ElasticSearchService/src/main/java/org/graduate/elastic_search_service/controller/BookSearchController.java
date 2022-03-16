package org.graduate.elastic_search_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.asyncsearch.AsyncSearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.graduate.elastic_search_service.constInfo.Const;
import org.graduate.elastic_search_service.data.AuthorBook;
import org.graduate.elastic_search_service.data.Book;
import org.graduate.elastic_search_service.entity.*;
import org.graduate.elastic_search_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/8
 * Time: 上午10:05
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookSearch")
public class BookSearchController {
    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    private final static int THRESHOLD = 10; // 列表查找的阈值

    @Resource(name = "JacksonMapper")
    private ObjectMapper mapper;

    /*
        由于 Spring WebFlux 对于 Form Data 的这种 “排斥性”，
        所以这里传入的请求参数不能放到请求 Body 内。

        对于参数对象的传入只能使用 @ModelAttribute 和 @RequestParam
        如果需要直接转换为对应的请求对象类型，
        请创建相应的组件类并实现对应的 Converter 接口
     */

    /**
     * 搜索书籍对象的访问接口
     *
     * @param searchPageStr    ： 查询的 SearchPage 对象参数
     * @param searchContentStr ： 搜索内容的 SearchContent 对象参数
     * @return : 得到的搜索结果对象
     */
    @PostMapping(path = "/searchBook",
            produces = "application/json") // 只针对 application/json 的请求体
    @CrossOrigin(origins = "*") // 允许跨域资源请求
    public ResultObject<Book>
    searchBook(
            @RequestParam(name = "searchPage") String searchPageStr,
            @RequestParam(name = "searchContent") String searchContentStr)
            throws JsonProcessingException {
        SearchPage searchPage = mapper.readValue(searchPageStr, SearchPage.class);
        SearchContent searchContent = mapper.readValue(searchContentStr, SearchContent.class);

        log.info("SearchPage: " + searchPage.toString());
        log.info("SearchContent: " + searchContent.toString());

        // 查找的请求的配置信息对象
        SearchSourceBuilder searchSourceBuilder =
                setSearchSourceBuilderBySearchPage(searchPage);
        // 设置全文匹配
        searchSourceBuilder.trackTotalHits(true);
        // 按照查找时的匹配分数从高到低排序， 尽管 ElasticSearch 的排序方式也是这样的
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

        // 输入的搜索文本类型
        ContentType type = searchContent.getType();
        // 输入的搜索文本内容
        String content = searchContent.getContent();

        // 按照输入的查询条件的内容来设置不同的搜索脚本
        if (ContentType.ISBN == type) {
            searchSourceBuilder.query(QueryBuilders
                    .regexpQuery(Const.BOOK_ISBN_STRING, ".*" + content + ".*"));
        } else if (ContentType.AuthorName == type) {
            searchSourceBuilder.query(QueryBuilders.matchQuery(
                    Const.AUTHOR_NAME_STRING, content));
        } else if (ContentType.BookName == type) {
            searchSourceBuilder.query(QueryBuilders.matchQuery(
                    Const.BOOK_NAME_STRING, content));
        } else {
            searchSourceBuilder.query(QueryBuilders
                    .multiMatchQuery(content,
                            Const.AUTHOR_NAME_STRING, Const.BOOK_NAME_STRING)
                    .type(MultiMatchQueryBuilder.Type.MOST_FIELDS)
            );
        }

        // 设置最大等待时间
        searchSourceBuilder.timeout(TimeValue
                .timeValueMillis(Const.BOOK_SEARCH_MAX_TIME_OUT_MILLI));

        // 设置查找书籍的请求对象
        SearchRequest request = new SearchRequest(Const.BOOK_SEARCH_INDEX);
        // 将请求信息添加到请求对象中
        request.source(searchSourceBuilder);

        return getResultObject(Book.class,
                Objects.requireNonNull(getSearchResponseByRequest(request)));
    }

    /**
     * 按照作者 Id 搜素对应的书籍信息
     *
     * @param searchPage ： 查询的 SearchPage 对象
     * @param authorId   ： 查询的作者 Id 对象
     * @return ： 得到的搜索结果对象
     */
    @PostMapping(path = "/searchBookByAuthorId/{authorId}")
    public ResultObject<AuthorBook> searchBookByAuthorId(
            @RequestBody SearchPage searchPage,
            @PathVariable(name = "authorId") Long authorId) {
        // 查找的请求的配置信息对象
        SearchSourceBuilder searchSourceBuilder =
                setSearchSourceBuilderBySearchPage(searchPage);
        // 设置全文匹配
        searchSourceBuilder.trackTotalHits(true);
        // 按照查找时的匹配分数从高到低排序， 尽管 ElasticSearch 的排序方式也是这样的
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        // 按照作者 ID 查找的请求信息
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(Const.AUTHOR_ID_STRING, authorId)));

        // 设置查找书籍的请求对象
        SearchRequest request =
                new SearchRequest(Const.AUTHOR_SEARCH_INDEX);

        // 将请求信息添加到请求对象中
        request.source(searchSourceBuilder);

        return getResultObject(AuthorBook.class,
                Objects.requireNonNull(getSearchResponseByRequest(request)));
    }


    /**
     * 通过设置了请求参数对象的请求对象进行访问请求，
     * 得到对应的相应体
     *
     * @param request ： 设置了请求参数的请求对象
     * @return ： 查找出现异常时 -> null; 否则为对应请求的响应体
     */
    private SearchResponse
    getSearchResponseByRequest(@NonNull SearchRequest request) {
        try {
             /*
                原本打算使用异步的执行方式来获取对应的查找结果，
                但是转念一想， 异步的执行方式在此处返回结果可能到不了原来的请求客户，
                因此这里采用的方式是同步的查找方式。
             */
//            log.debug("Request: " + request.buildDescription());
            return restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Receive Exception.");
            return null;
        }
    }

    /**
     * 检查是否是输入的 ISBN 号来查找对应的书籍信息，
     * 这里的检测将是检测输入是否包含有四个数字，
     * 因为四个数字已经可以确定是否使用的是 ISBN 号
     *
     * @param content ： 待检测的输入搜索条件
     * @return ： 输入是否为 ISBN 号
     */
    private Boolean mayIsbn(@NonNull String content) {
        Pattern isbnPattern = Pattern.compile("\\d{4}");
        Matcher matcher = isbnPattern.matcher(content);

        return matcher.find();
    }

    /**
     * 通过 SearchPage 参数对象设置对 ElasticSearch 请求的请求参数对象
     *
     * @param searchPage ： 对指定路径的请求 SearchPage 对象
     * @return ： 得到设置了 SearchPage 对象的 SearchSourceBuilder 对象
     */
    private SearchSourceBuilder
    setSearchSourceBuilderBySearchPage(@NonNull SearchPage searchPage) {
        // 查找的参数信息对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查找的开始位置索引
        searchSourceBuilder.from(searchPage.getStartLocation());
        // 本次查找的数据大小
        searchSourceBuilder.size(searchPage.getSize());

        return searchSourceBuilder;
    }

    /**
     * 通过异步的相应对象得到对应的 ResultObject 对象
     *
     * @param obj                 ： ResultObject 对象中的泛型类型
     * @param asyncSearchResponse ： 异步请求得到的响应对象
     * @param <T>                 ： ResultObject 对象中的泛型
     * @return ： 得到的 ResultObject 对象
     */
    private <T> ResultObject<T>
    getResultObjectAsync(@NonNull Class<T> obj,
                         @NonNull AsyncSearchResponse asyncSearchResponse) {
        try {
            return getResultObject(obj, asyncSearchResponse.getSearchResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对应的响应体转换为结果对象
     *
     * @param obj      ： 结果对象实体类的类型
     * @param response ： 从搜索中得到的响应体
     * @param <T>      ： ResultObject 中的实体类的类型
     * @return ： ResultObject 对象
     */
    private <T> ResultObject<T>
    getResultObject(@NonNull Class<T> obj,
                    @NonNull SearchResponse response) {
        ResultObject<T> resultObject = new ResultObject<>();

        // 查找命中的记录
        SearchHits searchHits = response.getHits();
        // 查找命中的统计信息
        TotalHits totalHits = searchHits.getTotalHits();

        // 这次查找的查找时间
        resultObject.setTook(response.getTook().getMillis());
        // 得到的相关的记录总数
        resultObject.setRecordNum(totalHits.value);
        // 匹配的模式名称：‘eq’ 表示全文匹配
        resultObject.setRelation(totalHits.relation.name());
        // 总的分片数量
        resultObject.setTotalShards(response.getTotalShards());
        // 成功的分片数量
        resultObject.setSuccessfulShards(response.getSuccessfulShards());
        // 失败的分片数量
        resultObject.setFailedShards(response.getFailedShards());

        // 查找到的实体类列表
        List<T> objList = new ArrayList<>();

        /*
            使用反射的方式来得到每个查找到的实体对象。
            这是由于 ElasticSearch 提供的 API 中提供了对于实体的属性的查找，
            同时， 由于这里的的列表的元素为泛型， 因此使用反射的方式是最好的解决方式
         */
        Field[] fields = obj.getDeclaredFields();
        // 遍历命中的实体对象
        try {
            for (SearchHit hit : searchHits.getHits()) {
                Map<String, Object> map = hit.getSourceAsMap();
                Constructor<T> constructor = obj.getConstructor();
                T t = constructor.newInstance();
                for (Field field : fields) {
                    setFieldAttr(field, t, map);
                }

                objList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Generic Object create failed");
            objList.clear();
            objList = new ArrayList<>();
        }

        resultObject.setObjectList(objList);

        return resultObject;
    }

    /**
     * 递归设置相关的字段属性
     *
     * @param field ： 设置的字段属性， 判断是否为基本的类型从而设置对应的属性值
     * @param o     ： 待设置的属性字段值的对象
     * @param map   ： 用于获取对应数据的 Map 集合
     * @throws IllegalAccessException  ： 当访问相关的字段属性出现异常时抛出
     * @throws NoSuchMethodException   ： 当访问复杂类型时无对应的无参构造函数抛出
     * @throws InstantiationException: 使用构造函数初始化时失败抛出
     */
    private void setFieldAttr(@NonNull Field field, @NonNull Object o,
                              @NonNull Map<String, Object> map)
            throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        // 如果是基础类型， 则直接设置对应的属性
        if (isBasicClass(field.getType())) {
            field.setAccessible(true);
            field.set(o, map.get(field.getName()));
            return;
        }

        // 获取这个字段的复合属性对象类型
        Class<?> type = field.getType();

        // 获取这个字段的字段列表
        Field[] fields = type.getDeclaredFields();
        // 这个字段如果是个复合对象的话， 那么它一定有对应的 Map 来获取字段值
        log.debug("class: " + map.get(field.getName()).getClass());
        if (!(map.get(field.getName()) instanceof Map)) {
            throw new IllegalArgumentException("Map get Field should is a Map Object.");
        }
        Map<String, Object> nextMap = (Map<String, Object>) map.get(field.getName());

        // 这个字段对象， 用户获取对应的属性值
        Constructor<?> constructor = type.getConstructor();
        Object object = constructor.newInstance();
        for (Field field1 : fields) {
            setFieldAttr(field1, object, nextMap);
        }
        log.debug("After set Object: " + object.toString());
        field.setAccessible(true);
        field.set(o, object);
    }

    /**
     * 判断输入的类型是否是基本的数据类型，
     * 这里的基本数据类型不是一般的原生类型如： byte、short、long 等类型，
     * 而是在原生类型的基础之上添加了 List 的类型的检测，
     * 这是由于 List 是可以直接设置相关的字段属性。
     * 之所以需要判断输入的类型是否为基础的类型是由于一般的复合类型不能直接设置字段属性，
     * 因此需要递归这些字段类型直到指定的字段类型为基础的类型从而设置地段属性值。因此，
     * 如果有一天你发现有更好的方法来处理这个问题， 那么完全不需要这个检测
     *
     * @param obj： 输入的对象类
     * @param <T>  ： 泛型 T 表示
     * @return ： 是否为基础的类型：可直接设置字段属性的。
     */
    private <T> Boolean isBasicClass(@NonNull Class<T> obj) {
        if (obj == Number.class) return true;
        if (obj == Boolean.class) return true;
        if (obj == Character.class) return true;
        if (obj == String.class) return true;
        return obj == java.util.List.class;
    }
}
