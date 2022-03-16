package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 作者介绍的查找对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/27
 * Time: 下午9:33
 */

@Data
public class AuthorIntro {
    /*
        作者信息的查找标签
     */
    private List<Tag> tags;

    /*
        作者介绍是否使用正则表达式获取信息，
        默认为否
     */
    private Boolean useRegex = false;

    /*
        如果使用正则表达式， 则使用的正则表达式的模板
     */
    private String authorIntroRegex;
}
