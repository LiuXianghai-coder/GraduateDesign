package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookMark;
import org.graduate.book_service.data.UserBookMarkPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : LiuXianghai on 2021/3/21
 * @Created : 2021/03/21 - 14:58
 * @Project : service
 */
@Repository
public interface UserBookMarkRepo extends
        CrudRepository<UserBookMark, UserBookMarkPK> {
    List<UserBookMark> findUserBookMarksByIsbn(@Param("isbn") Long isbn);

    /**
     * 统计当前的查找的书籍评分为 1 的人数
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 当前该书籍评分为 1 的人数
     */
    @Query(value = "SELECT count(*) FROM user_book_mark WHERE score=1 AND isbn=:isbn",
            nativeQuery = true)
    Integer countOfOneScoreByIsbn(@Param("isbn") Long isbn);

    /**
     * 统计当前的查找的书籍评分为 2 的人数
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 当前该书籍评分为 2 的人数
     */
    @Query(value = "SELECT count(*) FROM user_book_mark WHERE score=2 AND isbn=:isbn",
            nativeQuery = true)
    Integer countOfTwoScoreByIsbn(@Param("isbn") Long isbn);


    /**
     * 统计当前的查找的书籍评分为 3 的人数
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 当前该书籍评分为 3 的人数
     */
    @Query(value = "SELECT count(*) FROM user_book_mark WHERE score=3 AND isbn=:isbn",
            nativeQuery = true)
    Integer countOfThreeScoreByIsbn(@Param("isbn") Long isbn);


    /**
     * 统计当前的查找的书籍评分为 4 的人数
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 当前该书籍评分为 4 的人数
     */
    @Query(value = "SELECT count(*) FROM user_book_mark WHERE score=4 AND isbn=:isbn",
            nativeQuery = true)
    Integer countOfFourScoreByIsbn(@Param("isbn") Long isbn);


    /**
     * 统计当前的查找的书籍评分为 5 的人数
     * @param isbn ： 查找的书籍的 ISBN 号
     * @return ： 当前该书籍评分为 5 的人数
     */
    @Query(value = "SELECT count(*) FROM user_book_mark WHERE score=5 AND isbn=:isbn",
            nativeQuery = true)
    Integer countOfFiveScoreByIsbn(@Param("isbn") Long isbn);
}
