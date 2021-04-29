package org.graduate.service.repository;

import org.graduate.service.data_entity.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:44
 * @Project : GetDataService
 */
public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    /**
     * 通过出版社的名称来查找
     * @param publisherName：待查找的出版社的名称
     * @return ： 查找到的出版社对象
     */
    Publisher findPublisherByPublisherName(@Param("publisherName") String publisherName);

    /**
     * 使用 Pageable 对象对数据进行分页的查询，
     * 避免一次查询数据量太大以致内存溢出
     * @param page ： 查找的 Page 对象
     * @return ： 使用 Page 对象查找的出版社对象列表
     */
    List<Publisher> findAll(@Param("page") Pageable page);
}
