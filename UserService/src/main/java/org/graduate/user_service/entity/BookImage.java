package org.graduate.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/2
 * Time: 下午3:27
 */

@Data
@Entity
@Table(name = "book_image")
public class BookImage {
    @Id
    @Column(name = "book_image_id")
    private Long bookImageId;

    @Basic
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "isbn")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Book book;
}
