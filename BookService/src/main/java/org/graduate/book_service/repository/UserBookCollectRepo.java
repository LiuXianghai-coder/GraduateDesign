package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookCollection;
import org.graduate.book_service.data.UserBookCollectionPK;
import org.graduate.book_service.data.UserBookStar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/1
 * Time: 下午6:32
 */

@Repository
public interface UserBookCollectRepo extends
        CrudRepository<UserBookCollection, UserBookCollectionPK> {
    /**
     * 按照书籍的 ISBN 和用户 ID 来查找收藏信息对象
     *
     * @param isbn   ： 查找书记的 ISBN
     * @param userId ： 当前访问的用户 ID
     * @return ： 查找到的 UserBookCollection 对象，可能不存在
     */
    Optional<UserBookCollection> findUserBookCollectionByIsbnAndUserId(@Param("isbn") Long isbn,
                                                                       @Param("userId") Long userId);
}
