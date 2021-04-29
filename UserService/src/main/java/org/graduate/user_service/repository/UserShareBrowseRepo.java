package org.graduate.user_service.repository;

import org.graduate.user_service.entity.UserBookBrowse;
import org.graduate.user_service.entity.UserBookBrowsePK;
import org.graduate.user_service.entity.UserShareBrowse;
import org.graduate.user_service.entity.UserShareBrowsePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:46
 * @Project : user_service
 */
@Repository
public interface UserShareBrowseRepo
        extends CrudRepository<UserShareBrowse, UserShareBrowsePK> {
}
