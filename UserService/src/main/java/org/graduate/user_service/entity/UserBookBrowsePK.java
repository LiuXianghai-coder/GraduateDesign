package org.graduate.user_service.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:38
 * @Project : user_service
 */
public class UserBookBrowsePK implements Serializable {
    private long userId;
    private long isbn;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "isbn")
    @Id
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBookBrowsePK that = (UserBookBrowsePK) o;

        if (userId != that.userId) return false;
        if (isbn != that.isbn) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (isbn ^ (isbn >>> 32));
        return result;
    }
}
