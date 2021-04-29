package org.graduate.book_service.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 相关节点的指数，这是为了在得到不同图书之间相似度而创建的。
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/27
 * Time: 上午9:55
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RelateNode implements Comparable<RelateNode> {
    private Integer kindId;

    private Double relateRate;

    @Override
    public int compareTo(RelateNode o) {
        if (this.getRelateRate().equals(o.getRelateRate()))
            return 0;

        return (int) (this.relateRate * 100 - o.getRelateRate() * 100);
    }
}
