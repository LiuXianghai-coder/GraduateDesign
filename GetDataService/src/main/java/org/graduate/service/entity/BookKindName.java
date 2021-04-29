package org.graduate.service.entity;

import lombok.Data;

/**
 * 书籍的 isbn 与类型的对应关系
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/31
 * Time: 下午7:59
 */
@Data
public class BookKindName {
    /*
        书籍的 isbn 号
     */
    private Long isbn;

    /*
        对应的种类名称
     */
    private String kindName;
}
