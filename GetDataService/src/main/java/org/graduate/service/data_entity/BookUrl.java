package org.graduate.service.data_entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 12:20
 * @Project : GetDataService
 */
@Data
@Entity
@Table(name = "book_url", schema = "public", catalog = "recommend_system")
public class BookUrl {
    private long urlId;
    private String url;

    @Basic
    @Column(name = "hash_book_detail")
    private Short hashBookDetail;

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long getUrlId() {
        return urlId;
    }

    public void setUrlId(long urlId) {
        this.urlId = urlId;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookUrl bookUrl = (BookUrl) o;

        if (urlId != bookUrl.urlId) return false;
        if (url != null ? !url.equals(bookUrl.url) : bookUrl.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (urlId ^ (urlId >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
