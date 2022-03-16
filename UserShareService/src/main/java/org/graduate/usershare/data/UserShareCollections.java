package org.graduate.usershare.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.OffsetDateTime;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 13:54
 * @Project : usershare
 */
@Entity
@Table(name = "user_share_collections")
@IdClass(UserShareCollectionsPK.class)
@Data
public class UserShareCollections {
    @Id
    @Column(name = "share_id")
    private long shareId;

    @Id
    @Column(name = "user_id")
    private long userId;

    @Basic
    @Column(name = "collection_flag")
    private Boolean flag;

    @Basic
    @Column(name = "collection_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:SS")
    private OffsetDateTime collectionDate;
}
