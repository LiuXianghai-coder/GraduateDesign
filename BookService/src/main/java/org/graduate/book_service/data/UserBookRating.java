package org.graduate.book_service.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 上午11:28
 */

@Entity
@Table(name = "user_book_rating", schema = "public", catalog = "recommend_system")
@IdClass(UserBookRatingPK.class)
@Data
public class UserBookRating implements Serializable {
    private long userId;

    private long isbn;

    private Double rate;

    private OffsetDateTime rateTime;

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
    @Column(name = "rate")
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Basic
    @Column(name = "rate_time")
    public OffsetDateTime getRateTime() {
        return rateTime;
    }

    public void setRateTime(OffsetDateTime rateTime) {
        this.rateTime = rateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookRating that = (UserBookRating) o;

        if (userId != that.userId) return false;
        if (isbn != that.isbn) return false;
        if (!Objects.equals(rate, that.rate)) return false;
        return Objects.equals(rateTime, that.rateTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (rateTime != null ? rateTime.hashCode() : 0);
        return result;
    }
}
