package org.graduate.elastic_search_service.repository;

import org.graduate.elastic_search_service.data_entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Book 对象的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 20:19
 * @Project : GetDataService
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    @Query(value = "SELECT b.isbn AS isbn, b.book_name AS book_name, \n" +
            "       b.book_score AS book_score, b.introduction AS introduction\n" +
            "FROM created_book\n" +
            "         JOIN book b on b.isbn = created_book.isbn\n" +
            "WHERE author_id=:authorId", nativeQuery = true )
    List<Book> getBooksByAuthorId(@Param("authorId") long authorId);

    /**
     * 查找对应类别的书籍信息列表
     * @param kindId ： 图书种类 ID
     * @param threshold ： 阈值，每次查找的最大数量
     * @return ： 得到的 Book 列表
     */
    @Query(value = "SELECT book.* FROM book " +
            "JOIN book_holding_kind bhk ON book.isbn = bhk.isbn " +
            "AND book_kind_id=:kid ORDER BY book.book_score LIMIT :threshold",
                nativeQuery = true)
    List<Book> findBookByKindId(@Param("kid") Integer kindId, @Param("threshold") Integer threshold);
}
