package org.graduate.usershare.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/5
 * Time: 下午9:02
 */

public class UserShareBrowsePK implements Serializable {
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

        UserShareBrowsePK that = (UserShareBrowsePK) o;

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
