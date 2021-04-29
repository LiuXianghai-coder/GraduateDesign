package org.graduate.service.entity;

import lombok.Data;
import org.graduate.service.data.CreatedBook;
import org.graduate.service.data_entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍的信息对象
 *
 * @author : LiuXianghai on 2020/12/31
 * @Created : 2020/12/31 - 10:05
 * @Project : GetDataService
 */
@Data
public class BookInfo {
    // 书籍的基本信息
    private Book book = new Book();

    // 书籍的作者信息
    private List<Author> authorList = new ArrayList<>();

    // 书籍的图像信息
    private List<BookImage> bookImages = new ArrayList<>();

    // 书籍的章节信息
    private List<BookChapter> bookChapters = new ArrayList<>();

    // 书籍与作者之间的对应信息
    private List<CreatedBook> createdBookList = new ArrayList<>();

    // 当前图书的出版信息
    private PublishedBook publishedBook = new PublishedBook();

    // 书籍种类对应的信息列表
    private BookKindName bookKindName = new BookKindName();
}
