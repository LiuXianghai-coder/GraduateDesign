package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookComment;
import org.graduate.book_service.data.UserBookCommentPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookCommentRepo
        extends CrudRepository<UserBookComment, UserBookCommentPK> {
    /**
     * 查找指定的书籍的评论信息
     * @param isbn ： 查找的书籍的 isbn
     * @return ： 查找的 UserBookComment 对象列表
     */
    List<UserBookComment> findUserBookCommentByIsbnOrderByCommentDateDesc(@Param("isbn") Long isbn);

    /**
     * 按照书籍的 ISBN 和用户 ID 来查找评论信息对象
     *
     * @param isbn   ： 查找书记的 ISBN
     * @param userId ： 当前访问的用户 ID
     * @return ： 查找到的 UserBookStar 对象，可能不存在
     */
    List<UserBookComment> findUserBookCommentByIsbnAndUserId(@Param("isbn") Long isbn,
                                                             @Param("userId") Long userId);

    /**
     * 统计当前用户对于书籍的评论数量，这是由于不能将自增列作为主键，因此必须每次手动设置主键
     * @param userId ： 添加评论的用户 Id
     * @param isbn ： 评论的书籍的 ISBN
     * @return ： 得到的评论数量
     */
    @Query(value = "SELECT count(*) FROM user_book_comment WHERE user_id=:userId AND isbn=:isbn",
            nativeQuery = true)
    Integer countUserBookCommentByIsbnAndUserId(@Param("userId") Long userId,
                                                @Param("isbn") Long isbn);
}
