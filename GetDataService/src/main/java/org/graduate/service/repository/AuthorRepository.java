package org.graduate.service.repository;

import org.graduate.service.data_entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 作者信息对象的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 21:43
 * @Project : GetDataService
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findAuthorByAuthorName(@Param("authorName") String authorName);
}
