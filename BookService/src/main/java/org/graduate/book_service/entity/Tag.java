package org.graduate.book_service.entity;

import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * 选取元素时的选取标签， 在 application.yml 文件内将其定义在
 * getdata.platformList.platform.getElement下。
 *
 * 该类的主要目的是为了在第一次搜索关键字时查找图书店铺可以准确地定位到相关的元素
 * priority 指明了该查找标签的优先级， 在查找相关标签时会按照这个属性再一次进行从小到大的排序
 *
 * selectName 用于指定如何选取元素，格式为 {tag}@{{attrName}={attrValue1}\s{attrValue2}....}，
 * 例如：div@class=hello,
 * 则会在当前的优先级下查找属性 "class=hello" 的所有 div 元素
 *
 * @author : LiuXianghai on 2020/12/25
 * @Created : 2020/12/25 - 8:23
 * @Project : GetDataService
 */
@Data
public class Tag implements Comparable<Tag> {
    // 选取元素的优先级
    private Integer priority;

    // 元素的选取规则
    private String selectName;

    @Override
    public int compareTo(@NonNull Tag o) {
        return this.priority - o.priority;
    }
}
