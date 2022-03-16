package org.graduate.book_service.data;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:59
 */

@Data
@Entity
@Table(name = "created_book")
public class BookAuthor {
    @EmbeddedId
    private BookAuthorKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("isbn")
    @JoinColumn(name = "isbn")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;
}
