package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "isbn")
    @JsonProperty("isbn")
    private long isbn;

    @Basic
    @Column(name = "book_name")
    @JsonProperty("bookName")
    private String bookName;

    @Basic
    @Column(name = "introduction")
    @JsonProperty("introduction")
    private String introduction;

    @Basic
    @Column(name = "book_score")
    @JsonProperty("bookScore")
    private Double bookScore;

    /*
        @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private Set<BookAuthor> authorSet = new HashSet<>();
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "created_book",
            joinColumns = @JoinColumn(name = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonProperty("authorSet")
    private Set<Author> authorSet = new HashSet<>();

    /*
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "published_book",
                joinColumns = @JoinColumn(name = "isbn"),
                inverseJoinColumns = @JoinColumn(name = "publisher_id")
        )
        private List<Publisher> publisherSet = new ArrayList<>();
     */

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonProperty("bookPublisherSet")
    private Set<BookPublisher> bookPublisherSet = new HashSet<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @JsonProperty("bookImageList")
    private List<BookImage> bookImageList = new ArrayList<>();
}