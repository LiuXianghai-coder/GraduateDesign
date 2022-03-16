package org.graduate.elastic_search_service.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 书籍的作者信息对象，为了节省空间， 这个类将移除作者的介绍信息。
 * 节省空间的原因在于 ElasticSearch 对于容量有限制，因此在能够节省的情况下尽量节省。
 * 而且作者的介绍信息在这里是无关的属性
 *
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : ElasticSearchA Service
 */
@Data
public class Author {
    /*
        作者 ID， 作者的唯一标识
     */
    @JsonProperty("authorId")
    private Number authorId;

    /*
        作者名， 省略了之前的介绍字段属性
     */
    @JsonProperty("authorName")
    private String authorName;
}
