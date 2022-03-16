package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserShareCollections;
import org.graduate.usershare.data.UserShareCollectionsPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 13:53
 * @Project : usershare
 */
@Repository
public interface UserCollectReactRepo extends
        CrudRepository<UserShareCollections, UserShareCollectionsPK> {
    /**
     * 通过 userId 和 shredId 来搜索用户收藏的动态信息对象
     * @param userId ： 用户 ID
     * @param shareId ： 动态 ID
     * @return ：得到的用户收藏的动态信息对象
     */
    Optional<UserShareCollections> findUserShareCollectionsByShareIdAndUserId(@Param("userId") Long userId,
                                                                             @Param("shareId") Long shareId);

    /**
     * 计算当前用户是否收藏了对应的动态，使用计量的方式来统计
     * @param userId ： 传入的 UserId 查找参数
     * @param shareId ： 传入的动态 ID 参数
     * @return ： 得到的记录总数， 如果 > 0 则说明用户收藏了该动态
     */
    @Query(value = "SELECT count(1) FROM user_share_collections " +
            "WHERE user_id =:userId AND share_id=:shareId", nativeQuery = true)
    Long countOfUserIdAndShareId(@Param("userId") Long userId, @Param("shareId") Long shareId);
}
