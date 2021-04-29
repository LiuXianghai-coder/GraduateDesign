package org.graduate.book_service.repository;


import org.graduate.book_service.data.Book;
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
    @Query(value = "SELECT * FROM book ORDER BY book.book_score LIMIT 30", nativeQuery = true)
    List<Book> getAllBook();

    /**
     * 通过作者的 Id 获取该作者对应的所有书籍信息列表
     *
     * @param authorId ： 输入搜索的作者 ID
     * @return ： 该作者对应的书籍信息列表
     */
    @Query(value = "SELECT b.isbn AS isbn, b.book_name AS book_name, \n" +
            "       b.book_score AS book_score, b.introduction AS introduction\n" +
            "FROM created_book\n" +
            "         JOIN book b on b.isbn = created_book.isbn\n" +
            "WHERE author_id=:authorId", nativeQuery = true)
    List<Book> getBooksByAuthorId(@Param("authorId") long authorId);

    /**
     * 通过输入的 ISBN 来查找指定的 Book 对象
     *
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 得到的 Book 对象
     */
    Book findBookByIsbn(@Param("isbn") Long isbn);

    /**
     * 查找对应类别的书籍信息列表
     *
     * @param kindId    ： 图书种类 ID
     * @param threshold ： 阈值，每次查找的最大数量
     * @return ： 得到的 Book 列表
     */
    @Query(value = "SELECT book.* FROM book " +
            "JOIN book_holding_kind bhk ON book.isbn = bhk.isbn " +
            "AND book_kind_id=:kid ORDER BY book.book_score LIMIT :threshold",
            nativeQuery = true)
    List<Book> findBookByKindId(@Param("kid") Integer kindId, @Param("threshold") Integer threshold);
}
