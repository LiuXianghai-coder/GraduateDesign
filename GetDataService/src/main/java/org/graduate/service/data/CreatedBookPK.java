package org.graduate.service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/30
 * Time: 下午11:20
 */

public class CreatedBookPK implements Serializable {
    private long isbn;

    private long authorId;

    @Column(name = "isbn")
    @Id
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Column(name = "author_id")
    @Id
    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatedBookPK that = (CreatedBookPK) o;

        if (isbn != that.isbn) return false;
        if (authorId != that.authorId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) (authorId ^ (authorId >>> 32));
        return result;
    }
}
