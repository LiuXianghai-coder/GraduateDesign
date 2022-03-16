package org.graduate.service.repository;

import org.graduate.service.data_entity.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Book 对象的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 20:19
 * @Project : GetDataService
 */
public interface BookRepository extends CrudRepository<Book, Long> {
}
