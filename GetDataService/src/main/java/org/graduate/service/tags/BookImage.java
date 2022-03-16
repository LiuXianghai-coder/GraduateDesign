package org.graduate.service.tags;

import lombok.Data;
import org.graduate.service.constants.Const;
import org.graduate.service.entity.Tag;

import java.util.List;

/**
 * 图书的图像地址的查找标签信息
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 20:46
 * @Project : GetDataService
 */
@Data
public class BookImage {
    // 书籍的图像信息的查找标签信息列表
    private List<Tag> tags;

    /*
        对于内容的获取是否需要使用正则表达式的标记，
        默认为不使用， 即直接获取文本内容
     */
    private Boolean useRegex;

    /*
        如果需要使用正则表达式获取内容，
        这个属性就是对应的正则表达式
     */
    private String urlRegex;

    public BookImage() {
        this.useRegex = Const.DEFAULT_USE_REGEX_FLAG;
    }
}
