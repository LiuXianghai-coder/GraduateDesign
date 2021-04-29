package org.graduate.service.repository;

import org.graduate.service.data_entity.BookImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * 图书的图片地址的 JPA 数据管理接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 21:41
 * @Project : GetDataService
 */
public interface BookImageRepository extends CrudRepository<BookImage, Long> {

    /**
     * 通过图书的 ISBN 以及相关的图书图片地址查找相关的图书图像对象
     * @param isbn ： 查找的图书的 ISBN 号
     * @param imageUrl ：查找的图书的图像地址
     * @return ： 查找得到的 BookImage 对象
     */
    BookImage findBookImageByIsbnAndImageUrl(@Param("isbn") Long isbn,
                                             @Param("imageUrl") String imageUrl);
}