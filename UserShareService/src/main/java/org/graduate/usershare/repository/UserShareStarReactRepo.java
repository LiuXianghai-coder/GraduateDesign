package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserShareStar;
import org.graduate.usershare.data.UserShareStarPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:17
 * @Project : usershare
 */
@Repository
public interface UserShareStarReactRepo
        extends CrudRepository<UserShareStar, UserShareStarPK> {
    /** 按照用户 Id 和动态 Id 查找点赞信息
     * @param userId : 用户 ID
     * @param shareId ： 动态 ID
     * @return ： 得到的用户对于相关动态的点赞信息对象
     */
    Optional<UserShareStar> findUserShareStarByUserIdAndShareId(@Param("userId") Long userId,
                                                               @Param("shareId") Long shareId);

    /**
     * 统计当前用户于动态之间的关联关系， 从而得到是否点赞了该动态的结果
     * @param userId ： 输入的用户 Id 查询参数
     * @param shareId ： 输入的动态 Id 查询参数
     * @return ： 得到的记录条数， > 0 则说明已经点赞了该动态
     */
    @Query(value = "SELECT count(1) FROM user_share_star WHERE" +
            " user_id=:userId AND share_id=:shareId", nativeQuery = true)
    Long countOfUserIdAndShareId(@Param("userId") Long userId, @Param("shareId") Long shareId);
}
