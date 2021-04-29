package com.example.bookrecommend.service;

import com.example.bookrecommend.pojo.Book;
import com.example.bookrecommend.pojo.BookChapter;
import com.example.bookrecommend.pojo.BookDetail;
import com.example.bookrecommend.pojo.BookEntity;
import com.example.bookrecommend.pojo.UserBook;
import com.example.bookrecommend.pojo.UserBookCollection;
import com.example.bookrecommend.pojo.UserBookComment;
import com.example.bookrecommend.pojo.UserBookMark;
import com.example.bookrecommend.pojo.UserBookReview;
import com.example.bookrecommend.pojo.UserBookStar;
import com.example.bookrecommend.sington.HttpStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * 对应：BookService， 提供对书籍信息的处理接口
 * 端口：8081
 */
public interface BookService {
    /**
     * 通过输入的书籍的 ISBN 查找对应的书籍的章节信息对象列表
     * @param isbn ： 输入搜索的书籍的 ISBN
     * @return ： 得到的书籍章节信息列表
     */
    @Headers("Content-Type: application/json")
    @GET("/book/bookChapter/{isbn}")
    Call<List<BookChapter>> findBookChaptersByIsbn(@Path("isbn") Long isbn);


    /**
     * 获取指定书籍的相关评论信息
     * @param isbn ： 当前查找书籍评论的书籍的 isbn 号
     * @return : 得到的用户书籍评论信息对象列表
     */
    @Headers("Content-Type: application/json")
    @GET("/book/bookComment/{isbn}")
    Call<List<UserBookComment>> bookComments(@Path("isbn") Long isbn);

    /**
     * 获取指定 ISBN 对应的书评信息对象列表
     * @param isbn ： 查找对应的书评的书籍 ISBN 号
     * @return ：得到的最新的书评信息列表
     */
    @Headers("Content-Type: application/json")
    @GET("/book/bookReviews/{isbn}")
    Call<List<UserBookReview>> findUserBookReviewsByIsbn(@Path("isbn") Long isbn);

    /**
     * 通过书籍的 ISBN 获取对应的书籍信息
     * @param isbn ： 输入搜索的 ISBN
     * @return ： 得到的书籍信息对象
     */
    @Headers("Content-Type: application/json")
    @GET("/book/book/{isbn}")
    Call<BookDetail> findBookByIsbn(@Path("isbn") Long isbn);

    /**
     * 检查用户对于当前书籍的记录信息
     * @param isbn ： 当前书籍的 ISBN
     * @param userId ： 当前的用户 Id
     * @return ： 得到的 UserBook 对象
     */
    @Headers("Content-Type: application/json")
    @GET("/user/info/{isbn}/{userId}")
    Call<UserBook> userBookInfo(@Path("isbn") Long isbn,
                                @Path("userId") Long userId);

    /**
     * 保存一个用户上传的书评
     * @param review ： 用户上传的书评信息对象
     * @return ： 成功
     */
    @Headers("Content-Type: application/json")
    @POST("/book/addBookReviews")
    Call<HttpStatus> addBookReviews(@Body UserBookReview review);

    /**
     * 更新用户对于指定书籍的点赞信息
     * @param obj ： 待更新的 UserBookStar 对象
     * @return ： 处理处理后的 UserBookStar 对象
     */
    @Headers("Content-Type: application/json")
    @POST("/user/updateStar")
    Call<UserBookStar> updateStar(@Body UserBookStar obj);

    /**
     * 更新用户对于指定书籍的收藏信息
     * @param obj ： 待更新的 UserBookCollection 对象
     * @return ： 处理后的 UserBookCollection 对象
     */
    @Headers("Content-Type: application/json")
    @POST("/user/updateCollection")
    Call<UserBookCollection> updateCollection(@Body UserBookCollection obj);

    /**
     * 添加用户对于指定书籍的评论信息
     * @param obj ： 用户对于书籍的评论信息对象
     * @return ： 处理结果，成功则返回 Http 状态吗 200
     */
    @Headers("Content-Type: application/json")
    @POST("/user/addComment")
    Call<HttpStatus> addComment(@Body UserBookComment obj);

    /**
     * 保存用户对于书籍的评论信息
     * @param userBookMark ： 要保存的用户评论信息对象
     * @return ： 处理结果， 成功则返回 Http 状态码 200
     */
    @Headers("Content-Type: application/json")
    @POST("/book/saveBookMark")
    Call<String> saveBookMark(@Body UserBookMark userBookMark);

    /**
     * 按照书籍的 ISBN 来查找对应的书籍打分信息列表
     * @param isbn ： 待查找的书籍 ISBN
     * @return ： 得到的书籍的打分信息列表
     */
    @Headers("Content-Type: application/json")
    @GET("/book/bookMarks/{isbn}")
    Call<List<UserBookMark>> findUserBookMarksByIsbn(@Path("isbn") Long isbn);

    /**
     * 按照作者 Id 搜索对应的书籍信息
     * @param authorId : 输入的作者 ID 查询参数
     * @return ： 得到的书籍信息列表
     */
    @Headers("Content-Type: application/json")
    @GET("/book/searchBookByAuthorId/{authorId}")
    Call<List<Book>> searchBookByAuthorId(@Path("authorId") Long authorId);

    /**
     * 通过用户获取对应的书籍浏览记录
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 Book 集合
     */
    @Headers("Content-Type: application/json")
    @GET("/user/throughRecord/{userId}")
    Call<List<BookEntity>> throughRecord(@Path("userId") Long userId);

    /**
     * 通过用户获取已经收藏的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 Book 集合
     */
    @Headers("Content-Type: application/json")
    @GET("/user/collectedBook/{userId}")
    Call<List<BookEntity>> collectedBook(@Path("userId") Long userId);

    /**
     * 通过用户获取已经评论过的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 Book 集合
     */
    @Headers("Content-Type: application/json")
    @GET("/user/commentedBook/{userId}")
    Call<List<BookEntity>> commentedBook(@Path("userId") Long userId);

    /**
     * 通过用户获取已经点赞过的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 Book 集合
     */
    @Headers("Content-Type: application/json")
    @GET("/user/staredBook/{userId}")
    Call<List<BookEntity>> staredBook(@Path("userId") Long userId);

    /*
        默认初始界面，按照用户 ID 来搜索
     */
    @Headers("Content-Type: application/json")
    @POST("/book/defaultBookPage/{userId}")
    Call<List<BookEntity>> defaultBookPage(@Path("userId") Long userId);
}
