package org.graduate.book_service.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:52
 */

@Data
@Entity
@Table(name = "author")
public class Author {
    @Id
    @Column(name = "author_id")
    private long authorId;

    @Basic
    @Column(name = "author_name")
    private String authorName;

    @Basic
    @Column(name = "author_introduction")
    private String authorIntroduction;

    /*
        @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private Set<BookAuthor> bookSet = new HashSet<>();
     */

//    @ManyToMany(mappedBy = "authorSet",fetch = FetchType.EAGER)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private Set<Book> bookSet = new HashSet<>();
}
