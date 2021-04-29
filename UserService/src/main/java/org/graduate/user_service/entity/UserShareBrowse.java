package org.graduate.user_service.entity;

import lombok.Data;
import java.time.OffsetDateTime;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:38
 * @Project : user_service
 */
@Data
@Entity
@Table(name = "user_share_browse", schema = "public", catalog = "recommend_system")
@IdClass(UserShareBrowsePK.class)
public class UserShareBrowse {
    private long userId;

    private long shareId;

    private OffsetDateTime browseTime;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "share_id")
    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    @Basic
    @Column(name = "browse_time")
    public OffsetDateTime getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(OffsetDateTime browseTime) {
        this.browseTime = browseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShareBrowse that = (UserShareBrowse) o;

        if (userId != that.userId) return false;
        if (shareId != that.shareId) return false;
        return Objects.equals(browseTime, that.browseTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (shareId ^ (shareId >>> 32));
        result = 31 * result + (browseTime != null ? browseTime.hashCode() : 0);
        return result;
    }
}
