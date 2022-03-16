package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 书籍出版日期的查找对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/28
 * Time: 下午10:50
 */
@Data
public class PublisherDate {
    // 查找的出版日期的标签信息
    private List<Tag> tags;

    /*
        是否使用正则表达式获取数据信息，
        默认为不使用
     */
    private Boolean useRegex;

    /*
        如果使用正则表达式获取数据信息，
        则这个属性为对应的正则表达式匹配模式
    */
    private String publisherDateRegex;
}
