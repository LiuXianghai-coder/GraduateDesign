package org.graduate.elastic_search_service.data_entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Data
@Entity
@Table(name = "author")
public class Author {
    private long authorId;

    private String authorName;

    private String authorIntroduction;

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Basic
    @Column(name = "author_name")
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Basic
    @Column(name = "author_introduction")
    public String getAuthorIntroduction() {
        return authorIntroduction;
    }

    public void setAuthorIntroduction(String authorIntroduction) {
        this.authorIntroduction = authorIntroduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (authorId != author.authorId) return false;
        if (!Objects.equals(authorName, author.authorName)) return false;
        return Objects.equals(authorIntroduction, author.authorIntroduction);
    }

    @Override
    public int hashCode() {
        int result = (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (authorIntroduction != null ? authorIntroduction.hashCode() : 0);
        return result;
    }
}
