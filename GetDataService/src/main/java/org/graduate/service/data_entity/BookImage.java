package org.graduate.service.data_entity;

import lombok.ToString;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@ToString
@Entity
@Table(name = "book_image", schema = "public", catalog = "recommend_system")
public class BookImage {
    private long bookImageId;

    private long isbn;

    private String imageUrl;

    @Id
    @Column(name = "book_image_id")
    public long getBookImageId() {
        return bookImageId;
    }

    public void setBookImageId(long bookImageId) {
        this.bookImageId = bookImageId;
    }

    @Basic
    @Column(name = "ISBN")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookImage bookImage = (BookImage) o;

        if (bookImageId != bookImage.bookImageId) return false;
        if (isbn != bookImage.isbn) return false;
        if (imageUrl != null ? !imageUrl.equals(bookImage.imageUrl) : bookImage.imageUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bookImageId ^ (bookImageId >>> 32));
        result = 31 * result + (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
