package org.graduate.book_service.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:24
 */

@Data
public class UserBookCommentPK implements Serializable {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "isbn")
    private long isbn;

    @Id
    @Column(name = "comment_id")
    private int commentId;
}
