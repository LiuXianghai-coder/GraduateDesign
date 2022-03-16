package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 14:19
 * @Project : service
 */
@Entity
@Table(name = "user_book_review_comment", schema = "public", catalog = "recommend_system")
@IdClass(UserBookReviewCommentPK.class)
public class UserBookReviewComment {
    private long userId;

    private long bookReviewId;

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
    @Column(name = "book_review_id")
    public long getBookReviewId() {
        return bookReviewId;
    }

    public void setBookReviewId(long bookReviewId) {
        this.bookReviewId = bookReviewId;
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

        UserBookReviewComment that = (UserBookReviewComment) o;

        if (userId != that.userId) return false;
        if (bookReviewId != that.bookReviewId) return false;
        if (commentContent != null ? !commentContent.equals(that.commentContent) : that.commentContent != null)
            return false;
        if (commentDate != null ? !commentDate.equals(that.commentDate) : that.commentDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (bookReviewId ^ (bookReviewId >>> 32));
        result = 31 * result + (commentContent != null ? commentContent.hashCode() : 0);
        result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
        return result;
    }
}
