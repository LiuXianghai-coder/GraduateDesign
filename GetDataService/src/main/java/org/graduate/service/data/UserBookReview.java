package org.graduate.service.data;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 14:19
 * @Project : service
 */
@Data
@Entity
@Table(name = "user_book_review", schema = "public", catalog = "recommend_system")
public class UserBookReview {
    private long bookReviewId;

    @Basic
    @Column(name = "isbn")
    private long isbn;

    @Basic
    @Column(name = "user_id")
    private long userId;

    @Basic
    @Column(name = "write_date")
    private OffsetDateTime writeDate;

    private Integer starNum;

    private Integer commentNum;

    private String content;

    @Id
    @Column(name = "book_review_id")
    public long getBookReviewId() {
        return bookReviewId;
    }

    public void setBookReviewId(long bookReviewId) {
        this.bookReviewId = bookReviewId;
    }

    @Basic
    @Column(name = "star_num")
    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    @Basic
    @Column(name = "comment_num")
    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookReview that = (UserBookReview) o;

        if (bookReviewId != that.bookReviewId) return false;
        if (writeDate != null ? !writeDate.equals(that.writeDate) : that.writeDate != null) return false;
        if (starNum != null ? !starNum.equals(that.starNum) : that.starNum != null) return false;
        if (commentNum != null ? !commentNum.equals(that.commentNum) : that.commentNum != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bookReviewId ^ (bookReviewId >>> 32));
        result = 31 * result + (writeDate != null ? writeDate.hashCode() : 0);
        result = 31 * result + (starNum != null ? starNum.hashCode() : 0);
        result = 31 * result + (commentNum != null ? commentNum.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
