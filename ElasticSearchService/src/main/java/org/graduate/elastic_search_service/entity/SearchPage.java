package org.graduate.elastic_search_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查找的分页对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/8
 * Time: 上午10:26
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchPage implements Serializable {
    /*
        查找的开始位置
     */
    @JsonProperty("startLocation")
    private Integer startLocation = 0;

    /*
        查找的记录条数， 默认为 20
     */
    @JsonProperty("size")
    private Integer size = 20;

    /*
        排序的条目信息
     */
    @JsonProperty("sortCol")
    private String sortCol = "bookName";

    public static void main(String[] args) throws JsonProcessingException {
        SearchPage searchPage = new SearchPage();
        searchPage.setSize(20);
        searchPage.setSortCol("BookName");
        searchPage.setStartLocation(0);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(searchPage));

        SearchContent searchContent = new SearchContent();
        searchContent.setContent("book");
        searchContent.setType(ContentType.BookName);

        System.out.println(mapper.writeValueAsString(searchContent));
    }
}
