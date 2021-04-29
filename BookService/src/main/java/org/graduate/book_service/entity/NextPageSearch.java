package org.graduate.book_service.entity;

import lombok.Data;

import java.util.List;

/**
 * 下一个页面的查找对象.
 * 前提是这个平台的下一页面的翻页策略是使用超链接
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/1
 * Time: 上午10:12
 */
@Data
public class NextPageSearch {
    /*
        下一个页面的查找标签顺序
     */
    private List<Tag> tags;

    /*
        是否使用正则表达式获取书籍的下一页面的地址信息
     */
    private Boolean useRegex;

    /*
        如果使用正则表达式获取对应的信息，
        则这个是对应的正则表达式模式
     */
    private String searchRegex;
}
