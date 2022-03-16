package org.graduate.service.entity;

import lombok.Data;
import org.graduate.service.tags.*;

/**
 * 图书的具体细节信息
 *
 * @author : LiuXianghai on 2020/12/31
 * @Created : 2020/12/31 - 10:50
 * @Project : GetDataService
 */
@Data
public class BookDetail {
    /*
        查找图书 ISBN 的查找顺序
     */
    private ISBN isbn;

    /*
        查找图书的 BookName 查找对象, 即图书的书名查找信息对象
     */
    private BookName bookName;

    /*
        查找图书的 BookIntro 查找对象， 即图书的介绍信息查找对象
     */
    private BookIntro bookIntro;

    /*
        图书的图片的地址的标签选择对象
     */
    private BookImage bookImage;

    /*
        作者名的查找信息字段
     */
    private AuthorName authorName;

    /*
        作者介绍的查找对象
     */
    private AuthorIntro authorIntro;

    /*
        书籍章节目录的查找标签
     */
    private BookChapterTag bookChapterTag;

    /*
        出版社出版名称的查找对象
     */
    private PublisherName publisherName;

    /*
        出版日期的查找对象
     */
    private PublisherDate publisherDate;

    /*
        书籍种类的查找对象
     */
    private BookKind bookKind;

    // 该平台使用的 http 协议
    private String httpProtocol;

    private String urlRegex; // 替换的选择

    private Boolean useReplaceFlag; // 是否要替换相关路径
}
