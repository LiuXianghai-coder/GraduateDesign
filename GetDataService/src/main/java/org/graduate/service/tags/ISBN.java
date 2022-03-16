package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.constants.Const;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 图书 ISBN 的查找标签顺序
 *
 * @author : LiuXianghai on 2020/12/31
 * @Created : 2020/12/31 - 10:52
 * @Project : GetDataService
 */
@Data
public class ISBN {
    // 图书 ISBN 的查找标签顺序
    private List<Tag> tags;

    /*
        有的信息获取需要使用到正则表达式，
        useRegex 代表是否使用正则表达式获取信息
        默认为不使用
     */
    private Boolean useRegex;

    /*
        ISBN 的查找正则表达式模式，
        这是由于有的标签内的内容包含其他的文本内容，
        使用正则表达式取得需要的数据是可行的
     */
    private String isbnRegex;
}
