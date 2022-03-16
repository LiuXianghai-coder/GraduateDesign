package org.graduate.book_service.data;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:29
 */
@Data
public class UserBookCollectionPK implements Serializable {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "isbn")
    private long isbn;
}
