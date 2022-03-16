package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 获取出版社名称的信息对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/28
 * Time: 下午10:47
 */
@Data
public class PublisherName {
    // 查找的出版社的标签查找信息
    private List<Tag> tags;

    /*
        是否使用正则表达式获取数据信息，
        默认为不使用
     */
    private Boolean useRegex = false;

    /*
        如果使用正则表达式获取数据信息，
        则这个属性为对应的正则表达式匹配模式
    */
    private String publisherNameRegex;
}
