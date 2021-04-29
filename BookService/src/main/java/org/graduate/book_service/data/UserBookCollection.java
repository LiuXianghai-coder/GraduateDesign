package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */

@Data
@Entity
@Table(name = "user_book_collection", schema = "public", catalog = "recommend_system")
@IdClass(UserBookCollectionPK.class)
public class UserBookCollection {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "isbn")
    private long isbn;

    @Basic
    @Column(name = "is_collection")
    @JsonProperty("collection")
    private Boolean collection = false;

    @Basic
    @Column(name = "collection_date")
    @JsonProperty("collectDate")
    private OffsetDateTime collectDate;
}
