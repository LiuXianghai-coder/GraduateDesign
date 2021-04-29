package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserShare;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:12
 * @Project : usershare
 */
@Repository
public interface UserShareReactRepo
        extends CrudRepository<UserShare, Long> {
    /**
     * 得到默认的一条分页的动态数据
     *
     * @return ： 得到的分页的用户动态信息对象
     */
    @Query(value = "SELECT * FROM user_share LIMIT 20", nativeQuery = true)
    List<UserShare> findDefault();

    @Deprecated
    @Query(value = "SELECT * FROM user_share", nativeQuery = true)
    List<UserShare> findUserSharesByPage(@Param("page") Pageable page);

    /**
     * 在进行用户动态的查找时， 需要更新对应的点赞数、评论数以及收藏数
     *
     * @param shareId ： 待更新的动态的对应动态 ID
     */
    @Modifying
    @Transactional
    @Query(value = "SELECT update_user_share(?1)", nativeQuery = true)
    void updateUserShare(Long shareId);

    /**
     * 通过用户分享的动态 ID 来查找对应的用户动态对象
     *
     * @param shareId ： 输入查找条件的动态 ID
     * @return ： 得到的用户动态对象
     */
    Optional<UserShare> findUserSharesByShareId(@Param("shareId") Long shareId);

    /**
     * 通过用户 ID 得到他创建的动态的数量
     *
     * @param userId ： 待搜索的用户 ID
     * @return ： 得到该用户创建的动态总数
     */
    @Deprecated
    // 表结构改变
    Long countAllByUserId(@Param("userId") Long userId);
}
