package org.graduate.user_service.repository;

import org.graduate.user_service.entity.UserBookBrowse;
import org.graduate.user_service.entity.UserBookBrowsePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 10:44
 * @Project : user_service
 */
@Repository
public interface UserBookBrowseRepo
        extends CrudRepository<UserBookBrowse, UserBookBrowsePK> {
}
