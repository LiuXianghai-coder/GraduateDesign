package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 9:52
 * @Project : usershare
 */
@Repository
public interface UserInfoRectRepo extends CrudRepository<UserInfo, Long> {
}
