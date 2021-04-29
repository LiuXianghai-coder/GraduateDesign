package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */

@Data
@Entity
@Table(name = "user_book_comment", schema = "public", catalog = "recommend_system")
@IdClass(UserBookCommentPK.class)
public class UserBookComment {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "isbn")
    private long isbn;

    @Id
    @Column(name = "comment_id")
    private int commentId;

    @Basic
    @Column(name = "comment_content")
    private String commentContent;

    @Basic
    @Column(name = "comment_date")
    private OffsetDateTime commentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private BasicUserInfo userInfo = new BasicUserInfo();
}
