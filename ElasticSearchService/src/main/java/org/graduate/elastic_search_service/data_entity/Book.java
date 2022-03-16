package org.graduate.elastic_search_service.data_entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Entity
@Data
@Table(name = "book")
public class Book {
    private long isbn;

    private String bookName;

    private String introduction;

    private Short bookScore;

    @Id
    @Column(name = "isbn")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "book_name")
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Basic
    @Column(name = "introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Basic
    @Column(name = "book_score")
    public Short getBookScore() {
        return bookScore;
    }

    public void setBookScore(Short bookScore) {
        this.bookScore = bookScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (isbn != book.isbn) return false;
        if (!Objects.equals(bookName, book.bookName)) return false;
        if (!Objects.equals(introduction, book.introduction)) return false;
        return Objects.equals(bookScore, book.bookScore);
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (bookScore != null ? bookScore.hashCode() : 0);
        return result;
    }
}
