package org.graduate.book_service.repository;

import org.graduate.book_service.data.PublishedBook;

import org.graduate.book_service.data.PublishedBookPK;
import org.springframework.data.repository.CrudRepository;

/**
 * 图书的出版信息对象的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 21:45
 * @Project : GetDataService
 */
public interface PublisherBookRepository extends CrudRepository<PublishedBook, PublishedBookPK> {
}
