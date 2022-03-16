package org.graduate.service.data_entity;

import lombok.ToString;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Entity
@Table(name = "book_chapter", schema = "public", catalog = "recommend_system")
@IdClass(BookChapterPK.class)
@ToString
public class BookChapter {
    private long isbn;

    private int chapterId;

    private short chapterKind;

    private String chapterName;

    @Id
    @Column(name = "ISBN")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Id
    @Column(name = "chapter_id")
    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    @Basic
    @Column(name = "chapter_kind")
    public short getChapterKind() {
        return chapterKind;
    }

    public void setChapterKind(short chapterKind) {
        this.chapterKind = chapterKind;
    }

    @Basic
    @Column(name = "chapter_name")
    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookChapter that = (BookChapter) o;

        if (isbn != that.isbn) return false;
        if (chapterId != that.chapterId) return false;
        if (chapterKind != that.chapterKind) return false;
        if (chapterName != null ? !chapterName.equals(that.chapterName) : that.chapterName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + chapterId;
        result = 31 * result + (int) chapterKind;
        result = 31 * result + (chapterName != null ? chapterName.hashCode() : 0);
        return result;
    }
}
