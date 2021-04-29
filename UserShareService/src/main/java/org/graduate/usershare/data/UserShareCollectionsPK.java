package org.graduate.usershare.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 13:56
 * @Project : usershare
 */
@Data
public class UserShareCollectionsPK implements Serializable {
    @Id
    @Column(name = "share_id")
    private long shareId;

    @Id
    @Column(name = "user_id")
    private long userId;
}
