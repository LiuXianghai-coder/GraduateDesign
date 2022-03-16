package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 图书的介绍信息查找条件对象
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 19:45
 * @Project : GetDataService
 */
@Data
public class BookIntro {
    // 书籍的介绍信息的查找标签信息列表
    private List<Tag> tags;

    /*
        对于内容的获取是否需要使用正则表达式的标记，
        默认为不使用， 即直接获取文本内容
     */
    private Boolean useRegex;

    /*
        图书介绍信息的匹配文本的正则表达式
     */
    private String introRegex;

    public BookIntro() {
        this.useRegex = false;
    }
}
