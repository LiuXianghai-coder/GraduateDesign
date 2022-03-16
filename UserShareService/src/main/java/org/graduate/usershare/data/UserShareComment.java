package org.graduate.usershare.data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:16
 * @Project : usershare
 */
@Entity
@Table(name = "user_share_comment", schema = "public", catalog = "recommend_system")
@IdClass(UserShareCommentPK.class)
public class UserShareComment {
    private long userId;

    private long shareId;

    private int commentId;

    private String commentContent;

    private OffsetDateTime commentDate;

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

    @Id
    @Column(name = "comment_id")
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "comment_content")
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Basic
    @Column(name = "comment_date")
    public OffsetDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(OffsetDateTime commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShareComment that = (UserShareComment) o;

        if (userId != that.userId) return false;
        if (shareId != that.shareId) return false;
        if (commentId != that.commentId) return false;
        if (commentContent != null ? !commentContent.equals(that.commentContent) : that.commentContent != null)
            return false;
        if (commentDate != null ? !commentDate.equals(that.commentDate) : that.commentDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (shareId ^ (shareId >>> 32));
        result = 31 * result + commentId;
        result = 31 * result + (commentContent != null ? commentContent.hashCode() : 0);
        result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
        return result;
    }
}
