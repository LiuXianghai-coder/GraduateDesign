package org.graduate.http_dom_service.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/27
 * Time: 下午10:36
 */

@Entity
@Table(name = "author")
@Data
public class Author {
    private Long authorId;

    private String authorName;

    private String authorIntro;

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getAuthorId() {
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
    @Column(name = "author_intro")
    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (authorId != author.authorId) return false;
        if (authorName != null ? !authorName.equals(author.authorName) : author.authorName != null) return false;
        if (authorIntro != null ? !authorIntro.equals(author.authorIntro) : author.authorIntro != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (authorId ^ (authorId >>> 32));
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (authorIntro != null ? authorIntro.hashCode() : 0);
        return result;
    }
}
