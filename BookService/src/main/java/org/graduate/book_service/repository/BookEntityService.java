package org.graduate.book_service.repository;

import org.graduate.book_service.data.BookEntity;
import org.graduate.book_service.entity.RelateNode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 下午2:03
 */
@Service
public interface BookEntityService {
    /**
     * 按照输入的用户 ID 来查找浏览过的图书信息列表
     * @param userId ： 输入要查找的用户 ID
     * @return ： 查找到的图书实体信息列表
     */
    List<BookEntity> findThroughBooksByUserId(@Param("userId") Long userId);

    /**
     * 按照输入的用户 ID 来查找收藏过的图书信息列表
     * @param userId ： 输入要查找的用户 ID
     * @return ： 查找到的图书实体信息列表
     */
    List<BookEntity> findCollectedBooksByUserId(@Param("userId") Long userId);

    /**
     * 按照输入的用户 ID 来查找评论过的图书信息列表
     * @param userId ： 输入要查找的用户 ID
     * @return ： 查找到的图书实体信息列表
     */
    List<BookEntity> findCommentedBooksByUserId(@Param("userId") Long userId);

    /**
     * 按照输入的用户 ID 来查找点赞过的图书信息列表
     * @param userId ： 输入要查找的用户 ID
     * @return ： 查找到的图书实体信息列表
     */
    List<BookEntity> findStaredBooksByUserId(@Param("userId") Long userId);

    /**
     * 按照图书相关的节点来搜索相关的图书
     * @param nodes ： 相关的节点列表
     * @return ： 得到的图书信息列表
     */
    List<BookEntity> findBooksByRelateNodes(List<RelateNode> nodes);

    /**
     * 查找默认的图书信息列表
     * @return ： 得到的图书信息列表
     */
    List<BookEntity> findDefaultBooks();
}