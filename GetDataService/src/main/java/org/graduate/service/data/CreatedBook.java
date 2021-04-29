package org.graduate.service.data;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/30
 * Time: 下午11:20
 */
@Data
@Entity
@Table(name = "created_book", schema = "public", catalog = "recommend_system")
@IdClass(CreatedBookPK.class)
public class CreatedBook {
    private long isbn;

    private long authorId;

    @Id
    @Column(name = "isbn")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Id
    @Column(name = "author_id")
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

        CreatedBook that = (CreatedBook) o;

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
