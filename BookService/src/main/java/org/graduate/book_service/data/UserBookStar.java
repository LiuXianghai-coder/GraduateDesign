package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */
@Data
@Entity
@Table(name = "user_book_star", schema = "public", catalog = "recommend_system")
@IdClass(UserBookStarPK.class)
public class UserBookStar {
    @JsonProperty("userId")
    private long userId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("star")
    private Boolean star = false;

    @JsonProperty("starDate")
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
    @Column(name = "isbn")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "is_star")
    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    @Basic
    @Column(name = "star_date")
    public OffsetDateTime getStarDate() {
        return starDate;
    }

    public void setStarDate(OffsetDateTime starDate) {
        this.starDate = starDate;
    }
}
