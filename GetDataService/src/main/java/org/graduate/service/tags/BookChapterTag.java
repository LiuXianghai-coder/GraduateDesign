package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 书籍章节信息的查找标签
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/28
 * Time: 下午10:11
 */
@Data
public class BookChapterTag {
    /*
        书籍目录列表的查找标签
     */
    private List<Tag> tags;

    /*
        是否启用正则表达式获取数据，
        默认为不启用
     */
    private Boolean useRegex;

    /*
        如果使用正则表达式获取数据，
        则需要指定对应的查找的正则表达式模式
     */
    private String bookChapterRegex;
}
