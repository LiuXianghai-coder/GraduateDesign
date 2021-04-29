package org.graduate.service.data_entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
public class PublishedBookPK implements Serializable {
    private long isbn;

    private long publisherId;

    @Column(name = "ISBN")
    @Id
    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    @Column(name = "publisher_id")
    @Id
    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublishedBookPK that = (PublishedBookPK) o;

        if (isbn != that.isbn) return false;
        if (publisherId != that.publisherId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (isbn ^ (isbn >>> 32));
        result = 31 * result + (int) (publisherId ^ (publisherId >>> 32));
        return result;
    }
}
