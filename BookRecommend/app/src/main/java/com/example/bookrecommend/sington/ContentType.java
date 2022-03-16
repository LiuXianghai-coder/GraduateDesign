package com.example.bookrecommend.sington;

/**
 * 搜索书籍的内容的类型， 需要在搜索时即采取相关的措施来进行设置。
 * 这是为了减少服务器的计算量，从而保证更好的性能
 *
 * @author LiuXianghai
 * @Time: 2021年2月22日15:22:47
 * @Product: BookRecommend
 */
public enum ContentType {
    ISBN, // 书籍的 ISBN 类型
    AuthorName, // 作者名的类型
    BooName, // 书名
    MIX // 混合类型：作者名，书名，
}
