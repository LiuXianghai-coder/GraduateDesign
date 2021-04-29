package org.graduate.usershare.data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:12
 * @Project : usershare
 */
@Entity
@Table(name = "user_share_star", schema = "public", catalog = "recommend_system")
@IdClass(UserShareStarPK.class)
public class UserShareStar {
    private long userId;

    private long shareId;

    private Boolean starFlag;

    private OffsetDateTime starDate;

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
    @Column(name = "star_flag")
    public Boolean getStarFlag() {
        return starFlag;
    }

    public void setStarFlag(Boolean starFlag) {
        this.starFlag = starFlag;
    }

    @Basic
    @Column(name = "star_date")
    public OffsetDateTime getStarDate() {
        return starDate;
    }

    public void setStarDate(OffsetDateTime starDate) {
        this.starDate = starDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShareStar that = (UserShareStar) o;

        if (userId != that.userId) return false;
        if (shareId != that.shareId) return false;
        if (starFlag != null ? !starFlag.equals(that.starFlag) : that.starFlag != null) return false;
        if (starDate != null ? !starDate.equals(that.starDate) : that.starDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (shareId ^ (shareId >>> 32));
        result = 31 * result + (starFlag != null ? starFlag.hashCode() : 0);
        result = 31 * result + (starDate != null ? starDate.hashCode() : 0);
        return result;
    }
}
