package org.graduate.service.entity;

import lombok.Data;

/**
 * 选择的元素的信息
 *
 * @author : LiuXianghai on 2020/12/25
 * @Created : 2020/12/25 - 21:35
 * @Project : GetDataService
 */
@Data
public class Element {
    // 选取的元素的标签名
    private String tagName;

    // 选取的元素的属性名
    private String attrName;

    // 选取的标签的属性的值
    private String[] attrValue;

    // 是否是最后一个查找的标签
    private Boolean isFinalTag;

    // 是否为选取某个标签的属性，此时的选择标签应当是 tag@attr 形式的
    private Boolean isSelectAttr;
}
