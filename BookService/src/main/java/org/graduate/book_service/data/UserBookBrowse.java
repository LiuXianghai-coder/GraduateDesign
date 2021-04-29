package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 下午1:58
 */

@Entity
@Table(name = "user_book_browse", schema = "public", catalog = "recommend_system")
@IdClass(UserBookBrowsePK.class)
public class UserBookBrowse {
    private long userId;

    private long isbn;

    private OffsetDateTime browseTime;

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
    @Column(name = "browse_time")
    public OffsetDateTime getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(OffsetDateTime browseTime) {
        this.browseTime = browseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookBrowse that = (UserBookBrowse) o;

        if (userId != that.userId) return false;
        if (isbn != that.isbn) return false;
        return Objects.equals(browseTime, that.browseTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (browseTime != null ? browseTime.hashCode() : 0);
        return result;
    }
}
