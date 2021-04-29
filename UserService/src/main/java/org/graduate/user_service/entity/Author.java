package org.graduate.user_service.entity;

import lombok.Data;

import javax.persistence.*;

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
}
