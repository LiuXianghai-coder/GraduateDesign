package org.graduate.service.repository;

import org.graduate.service.data.UserBookMark;
import org.graduate.service.data.UserBookMarkPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : LiuXianghai on 2021/3/21
 * @Created : 2021/03/21 - 14:58
 * @Project : service
 */
@Repository
public interface UserBookMarkRepo extends
        CrudRepository<UserBookMark, UserBookMarkPK> {
    List<UserBookMark> findUserBookMarksByIsbn(@Param("isbn") Long isbn);
}
