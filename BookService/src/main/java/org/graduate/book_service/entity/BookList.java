package org.graduate.book_service.entity;

import lombok.Data;

import java.util.List;

/**
 * 通过第一次搜索得到的出版社的信息，
 * 继续查找该出版社出版的书籍
 *
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 16:32
 * @Project : BookService
 */
@Data
public class BookList {
    // 下一页数据的翻页方式， 这是由于每个电商的策略不一致所导致的
    private NextPageStrategy nextPageStrategy;

    // 标签的查找对象列表
    private List<Tag> tags;

    /* 如果是通过传入 URL 参数 进行翻页操作的话，
     * 则需要获取该出版社页面的最大页数
     */
    private List<Tag> maxPageTags;

    // 当前的页面的最大页数
    private Integer maxPage;
}
