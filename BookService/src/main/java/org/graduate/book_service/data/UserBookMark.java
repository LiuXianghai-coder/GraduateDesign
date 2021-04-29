package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 14:19
 * @Project : service
 */
@Data
@Entity
@Table(name = "user_book_mark", schema = "public", catalog = "recommend_system")
@IdClass(UserBookMarkPK.class)
public class UserBookMark {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("score")
    private short score;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("markDate")
    private OffsetDateTime markDate;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "isbn")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "score")
    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "mark_date")
    public OffsetDateTime getMarkDate() {
        return markDate;
    }

    public void setMarkDate(OffsetDateTime markDate) {
        this.markDate = markDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookMark that = (UserBookMark) o;

        if (userId != that.userId) return false;
        if (isbn != that.isbn) return false;
        if (score != that.score) return false;
        if (!Objects.equals(comment, that.comment)) return false;
        return Objects.equals(markDate, that.markDate);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) score;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (markDate != null ? markDate.hashCode() : 0);
        return result;
    }
}
