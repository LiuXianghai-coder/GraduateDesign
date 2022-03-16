package org.graduate.user_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/2
 * Time: 下午2:25
 */

@Data
@Entity
@Table(name = "published_book")
public class BookPublisher {
    @EmbeddedId
    private BookPublisherPk id;

    @ManyToOne
    @MapsId("isbn")
    @JoinColumn(name = "isbn")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    @ManyToOne
    @MapsId("publisherId")
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Basic
    @Column(name = "published_date")
    @JsonProperty("publishedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date publishedDate;


}
