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
@Table(name = "user_book_review_star", schema = "public", catalog = "recommend_system")
@IdClass(UserBookReviewStarPK.class)
public class UserBookReviewStar {
    private long userId;

    private long bookReviewId;

    private OffsetDateTime starDate;

    private Boolean starFlag;

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
    @Column(name = "star_date")
    public OffsetDateTime getStarDate() {
        return starDate;
    }

    public void setStarDate(OffsetDateTime starDate) {
        this.starDate = starDate;
    }

    @Basic
    @Column(name = "star_flag")
    public Boolean getStarFlag() {
        return starFlag;
    }

    public void setStarFlag(Boolean starFlag) {
        this.starFlag = starFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookReviewStar that = (UserBookReviewStar) o;

        if (userId != that.userId) return false;
        if (bookReviewId != that.bookReviewId) return false;
        if (starDate != null ? !starDate.equals(that.starDate) : that.starDate != null) return false;
        if (starFlag != null ? !starFlag.equals(that.starFlag) : that.starFlag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (bookReviewId ^ (bookReviewId >>> 32));
        result = 31 * result + (starDate != null ? starDate.hashCode() : 0);
        result = 31 * result + (starFlag != null ? starFlag.hashCode() : 0);
        return result;
    }
}
