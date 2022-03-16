package org.graduate.book_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.graduate.book_service.tools.TimeTools;
import org.graduate.book_service.constant.Const;
import org.graduate.book_service.data.*;
import org.graduate.book_service.entity.UserBook;
import org.graduate.book_service.repository.*;
import org.graduate.book_service.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.graduate.book_service.tools.Tools.doHanLPApi;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/2
 * Time: 下午3:43
 */

@Slf4j
@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserBookController {
    private final UserBookStarRepo userBookStarRepo;

    private final UserBookCollectRepo userBookCollectRepo;

    private final UserBookCommentRepo userBookCommentRepo;

    private final UserBookRatingRepo ratingRepo;

    private final BookEntityService bookEntityService;

    private final UserBookBrowseRepo userBookBrowseRepo;

    @Autowired
    public UserBookController(UserBookStarRepo userBookStarRepo,
                              UserBookCollectRepo userBookCollectRepo,
                              UserBookCommentRepo userBookCommentRepo,
                              UserBookRatingRepo ratingRepo,
                              UserBookBrowseRepo userBookBrowseRepo,
                              BookEntityService bookEntityService) {
        this.userBookStarRepo = userBookStarRepo;
        this.userBookCollectRepo = userBookCollectRepo;
        this.userBookCommentRepo = userBookCommentRepo;
        this.ratingRepo = ratingRepo;
        this.bookEntityService = bookEntityService;
        this.userBookBrowseRepo = userBookBrowseRepo;
    }

    /**
     * 检查用户对于当前书籍的记录信息
     * @param isbn ： 当前书籍的 ISBN
     * @param userId ： 当前的用户 Id
     * @return ： 得到的 UserBook 对象
     */
    @GetMapping(path = "/info/{isbn}/{userId}")
    public UserBook userBookInfo(@PathVariable(name = "isbn") Long isbn,
                                 @PathVariable(name = "userId") Long userId) {
        Optional<UserBookStar> bookStar = userBookStarRepo
                .findUserBookStarByIsbnAndUserId(isbn, userId);
        List<UserBookComment> bookCommentList = userBookCommentRepo
                .findUserBookCommentByIsbnAndUserId(isbn, userId);
        Optional<UserBookCollection> bookCollection = userBookCollectRepo
                .findUserBookCollectionByIsbnAndUserId(isbn, userId);

        UserBook userBook = new UserBook();
        userBook.setStar(bookStar.isPresent() && bookStar.get().getStar());
        userBook.setComment(bookCommentList.size() > 0);
        userBook.setCollection(bookCollection.isPresent() &&
                bookCollection.get().getCollection());

        return userBook;
    }

    /**
     * 更新用户对于指定书籍的点赞信息
     * @param userBookStar ： 待更新的 UserBookStar 对象
     * @return ： 处理结果，成功则返回 Http 状态吗 200
     */
    @PostMapping(path = "/updateStar")
    public UserBookStar updateStar(@RequestBody UserBookStar userBookStar){
        Optional<UserBookStar> obj = userBookStarRepo
                .findUserBookStarByIsbnAndUserId(userBookStar.getIsbn(),
                        userBookStar.getUserId());
        UserBookStar val;

        UserBookRating rating = new UserBookRating();
        rating.setUserId(userBookStar.getUserId());
        rating.setIsbn(userBookStar.getIsbn());
        rating.setRateTime(TimeTools.currentTime());

        if (obj.isPresent()) {
            val = obj.get();

            /*
                如果该当前用户对于该图书已经点赞了，那么说明用户当前是喜欢该图书的，
                否则，当前用户对于该图书的态度是否定的。
             */
            if (val.getStar()) rating.setRate(0.0);
            else rating.setRate(1.0);

            val.setStar(!val.getStar());
        } else {
            val = new UserBookStar();
            val.setStar(true);

            rating.setRate(1.0); // 第一次点赞用户的态度肯定是积极的
        }

        val.setIsbn(userBookStar.getIsbn());
        val.setStarDate(TimeTools.currentTime());
        val.setUserId(userBookStar.getUserId());

        userBookStarRepo.save(val);

        ratingRepo.save(rating);

        log.debug("useBrookStar: " + val.toString());

        return val;
    }

    /**
     * 更新用户对于指定书籍的收藏信息
     * @param obj ： 待更新的 UserBookCollection 对象
     * @return ： 处理结果，成功则返回 Http 状态吗 200
     */
    @PostMapping(path = "/updateCollection")
    public UserBookCollection updateCollection(@RequestBody UserBookCollection obj) {
        Optional<UserBookCollection> collection = userBookCollectRepo
                .findUserBookCollectionByIsbnAndUserId(obj.getIsbn(),
                        obj.getUserId());
        UserBookCollection val;

        UserBookRating rating = new UserBookRating();
        rating.setUserId(obj.getUserId());
        rating.setIsbn(obj.getIsbn());
        rating.setRateTime(TimeTools.currentTime());

        if (collection.isPresent()) {
            val = collection.get();
            /*
                如果当前用户已经收藏了该书籍，那么认为用户当前不会喜欢该图书，
                同样的，如果用户之前没有收藏该图书，那么则认为他们目前是喜欢该图书的。
             */
            if (val.getCollection()) rating.setRate(0.0);
            else rating.setRate(1.0);

            val.setCollection(!val.getCollection());
        } else {
            val = new UserBookCollection();
            val.setCollection(true);

            rating.setRate(1.0); // 第一次收藏时肯定是喜欢该图书的
        }

        val.setIsbn(obj.getIsbn());
        val.setUserId(obj.getUserId());
        val.setCollectDate(TimeTools.currentTime());

        userBookCollectRepo.save(val);

        ratingRepo.save(rating);

        return val;
    }

    /**
     * 添加用户对于指定书籍的评论信息
     * @param obj ： 用户对于书籍的评论信息对象
     * @return ： 处理结果，成功则返回 Http 状态吗 200
     */
    @PostMapping(path = "/addComment")
    public HttpStatus addComment(@RequestBody UserBookComment obj){
        Integer count = userBookCommentRepo
                .countUserBookCommentByIsbnAndUserId(
                        obj.getUserInfo().getUserId(),
                        obj.getIsbn()
                );
        log.info("Count: " + count);
        obj.setCommentId(++count);
        obj.setCommentDate(OffsetDateTime.now());

        log.info("UserBookComment: " + obj);

        Map<String,Object> params= new HashMap<>();
        params.put("text", obj.getCommentContent());

        UserBookRating rating = new UserBookRating();
        rating.setUserId(obj.getUserId());
        rating.setIsbn(obj.getIsbn());
        rating.setRateTime(TimeTools.currentTime());

        if (doHanLPApi(Const.URL, Const.URL, params)
                .contains(Const.POSITIVE_RESULT_REGEX)){
            rating.setRate(1.0);
        } else rating.setRate(0.0);

        log.info("rating: " + rating.toString());

        ratingRepo.save(rating);

        userBookCommentRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 通过用户获取对应的书籍浏览记录
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 BookEntity 列表
     */
    @GetMapping(path = "/throughRecord/{userId}")
    public List<BookEntity> throughRecord(@PathVariable(name = "userId") Long userId) {
        return bookEntityService.findThroughBooksByUserId(userId);
    }

    /**
     * 通过用户获取已经收藏的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 BookEntity 列表
     */
    @GetMapping(path = "/collectedBook/{userId}")
    public List<BookEntity> collectedBook(@PathVariable(name = "userId") Long userId) {
        return bookEntityService.findCollectedBooksByUserId(userId);
    }

    /**
     * 通过用户获取已经评论过的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 BookEntity 列表
     */
    @GetMapping(path = "/commentedBook/{userId}")
    public List<BookEntity> commentedBook(@PathVariable(name = "userId") Long userId) {
        return bookEntityService.findCommentedBooksByUserId(userId);
    }

    /**
     * 通过用户获取已经点赞过的图书
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 BookEntity 列表
     */
    @GetMapping(path = "/staredBook/{userId}")
    public List<BookEntity> staredBook(@PathVariable(name = "userId") Long userId) {
        return bookEntityService.findStaredBooksByUserId(userId);
    }
}
