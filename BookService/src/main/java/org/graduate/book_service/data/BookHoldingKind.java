package org.graduate.book_service.data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/27
 * Time: 上午9:33
 */

@Entity
@Table(name = "book_holding_kind", schema = "public", catalog = "recommend_system")
@IdClass(org.graduate.book_service.data.BookHoldingKindPK.class)
public class BookHoldingKind {
    private long isbn;

    private int bookKindId;

    @Id
    @Column(name = "isbn")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Id
    @Column(name = "book_kind_id")
    public int getBookKindId() {
        return bookKindId;
    }

    public void setBookKindId(int bookKindId) {
        this.bookKindId = bookKindId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookHoldingKind that = (BookHoldingKind) o;

        if (isbn != that.isbn) return false;
        if (bookKindId != that.bookKindId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + bookKindId;
        return result;
    }
}
