package org.graduate.book_service.repository;

import org.graduate.book_service.data.BookChapter;
import org.graduate.book_service.data.BookChapterPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 图书章节信息的 JPA 数据访问接口
 *
 * @author : LiuXianghai on 2021/1/2
 * @Created : 2021/01/02 - 21:44
 * @Project : GetDataService
 */
public interface BookChapterRepository extends CrudRepository<BookChapter, BookChapterPK> {
    /*
        通过 chapterId 和 isbn 来查找对应的 BookChapter对象，用于判断是否存在相应的记录
     */
    BookChapter findBookChapterByChapterIdAndIsbn(@Param("chapterId") Integer chapterId,
                                                  @Param("isbn") Long isbn);

    /**
     * 通过输入的 ISBN 来查找对应的书籍的章节信息
     * @param isbn ： 输入的书籍的 ISBN
     * @return ：得到的书籍章节对象的列表
     */
   List<BookChapter> findByIsbn(@Param("isbn") Long isbn);
}
