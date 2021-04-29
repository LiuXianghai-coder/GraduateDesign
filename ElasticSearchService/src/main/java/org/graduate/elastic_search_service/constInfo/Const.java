package org.graduate.elastic_search_service.constInfo;

/**
 * 为了避免硬编码， 设置一些常量信息
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/10
 * Time: 下午4:28
 */

public class Const {
    /*
        对于使用弹性搜索的书籍的章节信息， 这个为对应的查找的索引地址
     */
    public final static String BOOK_CHAPTER_INDEX = "chapter";

    /*
        书籍的查找信息索引位置
     */
    public final static String BOOK_SEARCH_INDEX = "book";

    /*
        按照书籍的作者 ID 进行查找
     */
    public final static String AUTHOR_SEARCH_INDEX = "author";

    /*
        作者 ID 的字段的字符串形式
     */
    public final static String AUTHOR_ID_STRING = "authorId";

    /*
        书籍的 isbn 的字符串表示形式
     */
    public final static String BOOK_ISBN_STRING = "isbn";

    /*
        书籍章节信息搜索每次查找时的最大等待时间，单位为毫秒
     */
    public final static long BOOK_CHAPTER_MAX_TIME_OUT_MILLI = 500L;

    /*
        作者名的字符串表示形式
     */
    public final static String AUTHOR_NAME_STRING = "authorName";

    /*
        书籍名的字符串表示形式
     */
    public final static String BOOK_NAME_STRING = "bookName";

    /*
        查找时的最大等待时间， 单位为 ms
     */
    public final static long BOOK_SEARCH_MAX_TIME_OUT_MILLI = 2000L;
}
