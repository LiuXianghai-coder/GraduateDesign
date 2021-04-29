package org.graduate.service.data_entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Entity
@Table(name = "publisher")
public class Publisher {
    private long publisherId;
    private String publisherName;

    @Id
    @Column(name = "publisher_id")
    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    @Basic
    @Column(name = "publisher_name")
    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher publisher = (Publisher) o;

        if (publisherId != publisher.publisherId) return false;
        return Objects.equals(publisherName, publisher.publisherName);
    }

    @Override
    public int hashCode() {
        int result = (int) (publisherId ^ (publisherId >>> 32));
        result = 31 * result + (publisherName != null ? publisherName.hashCode() : 0);
        return result;
    }
}
