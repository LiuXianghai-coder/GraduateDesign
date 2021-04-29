package org.graduate.service.data_entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.Instant;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Entity
@Table(name = "published_book", schema = "public", catalog = "recommend_system")
@IdClass(PublishedBookPK.class)
public class PublishedBook {
    private long isbn;

    private long publisherId;

    private Date publishedDate;

    @Id
    @Column(name = "ISBN")
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Id
    @Column(name = "publisher_id")
    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    @Basic
    @Column(name = "published_date")
    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublishedBook that = (PublishedBook) o;

        if (isbn != that.isbn) return false;
        if (publisherId != that.publisherId) return false;
        if (publishedDate != null ? !publishedDate.equals(that.publishedDate) : that.publishedDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) (publisherId ^ (publisherId >>> 32));
        result = 31 * result + (publishedDate != null ? publishedDate.hashCode() : 0);
        return result;
    }
}
