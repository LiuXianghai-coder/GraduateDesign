package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 作者姓名的查找方式
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/27
 * Time: 下午9:13
 */
@Data
public class AuthorName {
    /*
        作者名的查找标签顺序
     */
    private List<Tag> tags;

    /*
        是否使用正则表达式匹配数据，默认为不使用
     */
    private Boolean useRegex = false;

    /*
        如果使用正则表达式的话，
        这个属性则为正则表达式的匹配模式
     */
    private String authorRegex;
}
