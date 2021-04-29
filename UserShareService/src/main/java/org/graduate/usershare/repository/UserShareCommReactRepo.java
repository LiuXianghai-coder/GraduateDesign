package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserShareComment;
import org.graduate.usershare.data.UserShareCommentPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:13
 * @Project : usershare
 */
@Repository
public interface UserShareCommReactRepo
        extends CrudRepository<UserShareComment, UserShareCommentPK> {
    /**
     * 统计当前用户对于指定的动态由多少条评论信息
     * @param userId ： 用户 ID
     * @param shareId ：动态 ID
     * @return ： 用户对于该动态的评论数
     */
    Integer countUserShareCommentByUserIdAndShareId(@Param("userId") Long userId,
                                                       @Param("shareId") Long shareId);
}
