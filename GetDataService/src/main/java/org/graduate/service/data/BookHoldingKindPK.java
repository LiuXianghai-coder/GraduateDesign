package org.graduate.service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/31
 * Time: 下午7:35
 */

public class BookHoldingKindPK implements Serializable {
    private long isbn;

    private Integer bookKindId;

    @Column(name = "ISBN")
    @Id
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Column(name = "book_kind_id")
    @Id
    public Integer getBookKindId() {
        return bookKindId;
    }

    public void setBookKindId(Integer bookKindId) {
        this.bookKindId = bookKindId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookHoldingKindPK that = (BookHoldingKindPK) o;

        if (isbn != that.isbn) return false;
        if (bookKindId != that.bookKindId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) bookKindId;
        return result;
    }
}
