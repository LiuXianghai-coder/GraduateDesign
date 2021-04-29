package org.graduate.book_service.data;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 上午11:29
 */
@Data
public class UserBookRatingPK implements Serializable {
    @Column(name = "user_id")
    private long userId;

    @Column(name = "isbn")
    private long isbn;
}
