package org.graduate.elastic_search_service.repository;

import org.graduate.elastic_search_service.data_entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * 作者信息对象的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 21:43
 * @Project : GetDataService
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findAuthorByAuthorName(@Param("authorName") String authorName);
}
