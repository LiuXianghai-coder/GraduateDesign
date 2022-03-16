package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 书籍种类的查找对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/31
 * Time: 下午7:30
 */
@Data
public class BookKind {
    /*
        书籍的介绍信息的查找标签信息列表
     */
    private List<Tag> tags;

    /*
        对于内容的获取是否需要使用正则表达式的标记，
        默认为不使用， 即直接获取文本内容
     */
    private Boolean useRegex = false;

    /*
        图书介绍信息的匹配文本的正则表达式
     */
    private String bookKindRegex;
}
