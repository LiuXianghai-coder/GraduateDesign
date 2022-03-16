package org.graduate.elastic_search_service.entity;

/**
 * 输入的搜索内容的类型
 *
 * @author : LiuXianghai on 2021/2/21
 * @Created : 2021/02/21 - 16:45
 * @Project : elastic_search_service
 */
public enum ContentType {
    ISBN, // 书籍的 ISBN 类型
    AuthorName, // 作者名的类型
    BookName, // 书名
    MIX // 混合类型：作者名，书名，
}
