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
@Table(name = "book_kind", schema = "public", catalog = "recommend_system")
public class BookKind {
    private Integer bookKindId;

    private String bookKindName;

    @Id
    @Column(name = "book_kind_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getBookKindId() {
        return bookKindId;
    }

    public void setBookKindId(Integer bookKindId) {
        this.bookKindId = bookKindId;
    }

    @Basic
    @Column(name = "book_kind_name")
    public String getBookKindName() {
        return bookKindName;
    }

    public void setBookKindName(String bookKindName) {
        this.bookKindName = bookKindName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookKind bookKind = (BookKind) o;

        if (bookKindId != bookKind.bookKindId) return false;
        if (bookKindName != null ? !bookKindName.equals(bookKind.bookKindName) : bookKind.bookKindName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) bookKindId;
        result = 31 * result + (bookKindName != null ? bookKindName.hashCode() : 0);
        return result;
    }
}
