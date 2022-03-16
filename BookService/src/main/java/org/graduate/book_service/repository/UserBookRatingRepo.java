package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookRating;
import org.graduate.book_service.data.UserBookRatingPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 上午11:31
 */
@Repository
public interface UserBookRatingRepo
        extends CrudRepository<UserBookRating, UserBookRatingPK> {
    /**
     * 查找指定用户的近期喜欢的图书
     * @param userId ： 传入的用户 ID
     * @return ： 得到的 UserBookRating对象
     */
    @Query(value = "SELECT * FROM user_book_rating WHERE user_id=:userId AND rate > 0.5 " +
            "ORDER BY rate DESC , rate_time DESC LIMIT 1",
            nativeQuery = true)
    Optional<UserBookRating> findRecentLikeBook(@Param("userId") Long userId);
}
