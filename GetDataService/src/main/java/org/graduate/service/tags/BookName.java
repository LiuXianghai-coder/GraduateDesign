package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.constants.Const;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 图书的书名查找方式
 * @author : LiuXianghai on 2020/12/31
 * @Created : 2020/12/31 - 17:47
 * @Project : GetDataService
 */
@Data
public class BookName {
    // 书籍的标签查找顺序
    private List<Tag> tags;

    /*
        对于内容的获取是否需要使用正则表达式的标记，
        默认为不使用， 即直接获取文本内容
     */
    private Boolean useRegex;

    /*
        如果使用正则表达式获取信息， 那么这个
        属性将会是匹配信息的正则表达式
     */
    private String bookNameRegex;

    public BookName() {
        this.useRegex = Const.DEFAULT_USE_REGEX_FLAG;
    }
}
