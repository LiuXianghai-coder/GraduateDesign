package org.graduate.book_service.data;

import jdk.jfr.Enabled;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/2
 * Time: 下午2:23
 */

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Enabled
public class BookPublisherPk implements Serializable {
    @Column(name = "isbn")
    private long isbn;

    @Column(name = "publisher_id")
    private long publisherId;
}
