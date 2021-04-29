package org.graduate.book_service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */

public class UserBookStarPK implements Serializable {
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

        UserBookStarPK that = (UserBookStarPK) o;

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
