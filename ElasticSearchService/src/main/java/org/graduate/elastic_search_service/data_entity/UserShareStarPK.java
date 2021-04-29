package org.graduate.elastic_search_service.data_entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:12
 * @Project : usershare
 */
public class UserShareStarPK implements Serializable {
    private long userId;
    private long shareId;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "share_id")
    @Id
    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShareStarPK that = (UserShareStarPK) o;

        if (userId != that.userId) return false;
        if (shareId != that.shareId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (shareId ^ (shareId >>> 32));
        return result;
    }
}
