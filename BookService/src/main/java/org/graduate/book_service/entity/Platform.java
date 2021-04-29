package org.graduate.book_service.entity;

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

    // 该平台的基础地址， 如 http://search.dangdang.com 等
    private String basicUrl;

    // 平台的查找基础地址
    private List<String> urls;

    // 第一次查找的关键字
    private String keyWord;

    // 该平台使用的 http 协议
    private String httpProtocol;

    // 根据出版社查找对应的书籍列表
    private BookList bookList;

    // 下一页面的查找对象， 前提是该平台使用超链接的方式获取对应的下一页面
    private NextPageSearch nextPageSearch;
}
