package org.graduate.elastic_search_service.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍的作者信息对象，为了节省空间， 这个类将移除作者的介绍信息。
 * 节省空间的原因在于 ElasticSearch 对于容量有限制，因此在能够节省的情况下尽量节省。
 * 而且作者的介绍信息在这里是无关的属性
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/11
 * Time: 上午11:42
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthorSimple {
    private Number authorId;

    private String authorName;
}
