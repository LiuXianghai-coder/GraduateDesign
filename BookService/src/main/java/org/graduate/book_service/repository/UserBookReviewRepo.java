package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookReview;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 14:20
 * @Project : service
 */
@Repository
public interface UserBookReviewRepo extends CrudRepository<UserBookReview, Long> {
    /**
     * 通过对应书籍的 ISBN 来查找指定的书评信息
     * @param isbn ： 当前查找的书籍 ISBN
     * @return : 得到的书评对象列表
     */
    List<UserBookReview> findUserBookReviewsByIsbn(@Param("isbn") Long isbn);

    /**
     * 通过对应用户的 id 来查找指定的书评信息
     * @param userId ： 当前查找的用户的 ID
     * @return : 得到的书评对象列表
     */
    List<UserBookReview> findUserBookReviewByUserId(@Param("userId") Long userId);

    /**
     * 在获取书评信息时需要更新一下总的点赞数和评论数
     * @param reviewId ： 对应的书评 ID
     */
    @Deprecated
    @Modifying
    @Transactional
    @Query(value = "SELECT update_user_book_review(:reviewId)", nativeQuery = true)
    void updateUserBookReview(@Param("reviewId") Long reviewId);
}