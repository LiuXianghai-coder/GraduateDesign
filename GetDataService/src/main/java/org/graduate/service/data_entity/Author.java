package org.graduate.service.data_entity;

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
    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorId;

    @Basic
    @Column(name = "author_name")
    private String authorName;

    @Basic
    @Column(name = "author_introduction")
    private String authorIntroduction;
}
