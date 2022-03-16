package org.graduate.usershare.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:16
 * @Project : usershare
 */
public class UserShareCommentPK implements Serializable {
    private long userId;
    private long shareId;
    private Integer commentId;

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

    @Column(name = "comment_id")
    @Id
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShareCommentPK that = (UserShareCommentPK) o;

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
