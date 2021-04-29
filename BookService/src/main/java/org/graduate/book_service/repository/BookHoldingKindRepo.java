package org.graduate.book_service.repository;

import org.graduate.book_service.data.BookHoldingKind;
import org.graduate.book_service.data.BookHoldingKindPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/27
 * Time: 上午9:34
 */

@Repository
public interface BookHoldingKindRepo
        extends CrudRepository<BookHoldingKind, BookHoldingKindPK> {

    /**
     * 查找图书所属的种类信息
     * @param isbn ： 输入的图书的 isbn
     * @return ： 得到的图书种类信息对象
     */
    @Query(value = "SELECT * FROM book_holding_kind WHERE isbn=:isbn LIMIT 1",
            nativeQuery = true)
    Optional<BookHoldingKind> findBookHoldingKindByIsbn(@Param("isbn") Long isbn);
}
