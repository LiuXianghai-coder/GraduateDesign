package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookStar;
import org.graduate.book_service.data.UserBookStarPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBookStarRepo
        extends CrudRepository<UserBookStar, UserBookStarPK> {
    /**
     * 按照书籍的 ISBN 和用户 ID 来查找点赞信息对象
     * @param isbn ： 查找书记的 ISBN
     * @param userId ： 当前访问的用户 ID
     * @return ： 查找到的 UserBookStar 对象，可能不存在
     */
    Optional<UserBookStar> findUserBookStarByIsbnAndUserId(@Param("isbn") Long isbn,
                                                           @Param("userId") Long userId);
}
