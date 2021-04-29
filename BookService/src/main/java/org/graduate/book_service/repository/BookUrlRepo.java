package org.graduate.book_service.repository;

import org.graduate.book_service.data.BookUrl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 13:03
 * @Project : GetDataService
 */
@Repository
public interface BookUrlRepo extends CrudRepository<BookUrl, Long> {
    /**
     * 查询从开始位置得到的书籍的地址
     * @param start ： 开始位置索引
     * @param size ： 本次查询的内容大小
     * @return ： 得到的 BookUrl 列表对象
     */
    @Query(value = "SELECT * FROM book_url LIMIT :size OFFSET :start", nativeQuery = true)
    List<BookUrl> findBookUrlsByStartAndEnd(@Param("start") Long start, @Param("size") Long size);
}
