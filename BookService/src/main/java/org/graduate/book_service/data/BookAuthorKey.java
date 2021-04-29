package org.graduate.book_service.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:55
 */

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class BookAuthorKey implements Serializable {
    @Column(name = "isbn")
    private Long isbn;

    @Column(name = "author_id")
    private Long authorId;
}
