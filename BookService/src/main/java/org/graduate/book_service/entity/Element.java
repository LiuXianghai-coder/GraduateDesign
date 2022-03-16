package org.graduate.book_service.entity;

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
}
