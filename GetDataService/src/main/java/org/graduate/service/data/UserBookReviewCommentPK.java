package org.graduate.service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 14:19
 * @Project : service
 */
public class UserBookReviewCommentPK implements Serializable {
    private long userId;
    private long bookReviewId;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "book_review_id")
    @Id
    public long getBookReviewId() {
        return bookReviewId;
    }

    public void setBookReviewId(long bookReviewId) {
        this.bookReviewId = bookReviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookReviewCommentPK that = (UserBookReviewCommentPK) o;

        if (userId != that.userId) return false;
        if (bookReviewId != that.bookReviewId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (bookReviewId ^ (bookReviewId >>> 32));
        return result;
    }
}
