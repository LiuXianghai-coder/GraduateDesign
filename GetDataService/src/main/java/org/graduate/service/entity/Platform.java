package org.graduate.service.entity;

import lombok.Data;

import java.util.List;

/**
 * 获取数据的平台信息，
 * 包括： 平台名称、URL、搜索的关键字、获取元素的标签信息
 *
 * 具体配置信息：application.yml
 */
@Data
public final class Platform {
    // 该平台对应的 ID
    private Short platFormId;

    // 平台的名称
    private String name;

    // 平台的查找基础地址
    private List<String> urls;

    // 第一次查找的关键字
    private String keyWord;

    // 根据出版社查找对应的书籍列表
    private BookList bookList;

    // 获取元素的顺序
    private List<Tag> tags;

    // 图书的详细信息
    private BookDetail bookDetail;
}
