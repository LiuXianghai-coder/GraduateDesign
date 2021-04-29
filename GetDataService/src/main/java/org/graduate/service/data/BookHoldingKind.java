package org.graduate.service.data;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/31
 * Time: 下午7:35
 */
@Data
@Entity
@Table(name = "book_holding_kind",
        schema = "public", catalog = "recommend_system")
@IdClass(BookHoldingKindPK.class)
public class BookHoldingKind {
    private long isbn;

    private Integer bookKindId;

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

        BookHoldingKind that = (BookHoldingKind) o;

        if (isbn != that.isbn) return false;
        return bookKindId == that.bookKindId;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) bookKindId;
        return result;
    }
}
