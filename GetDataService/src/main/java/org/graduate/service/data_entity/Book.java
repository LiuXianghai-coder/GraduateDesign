package org.graduate.service.data_entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 * @Project : GetDataService
 */
@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "isbn")
    private long isbn;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "book_score")
    private Short bookScore;

    @Column(name = "href")
    private String href;
}
