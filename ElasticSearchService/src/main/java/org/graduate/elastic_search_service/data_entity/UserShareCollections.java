package org.graduate.elastic_search_service.data_entity;

import lombok.Data;

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
}
