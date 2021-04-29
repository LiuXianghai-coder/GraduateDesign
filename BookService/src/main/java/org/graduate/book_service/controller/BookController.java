package org.graduate.book_service.controller;

import org.graduate.book_service.tools.TimeTools;
import org.graduate.book_service.constant.Const;
import org.graduate.book_service.data.*;
import org.graduate.book_service.entity.BookDetail;
import org.graduate.book_service.entity.RelateNode;
import org.graduate.book_service.repository.*;
import org.graduate.book_service.tools.SaveBookUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 13:44
 * @Project : service
 */
@RestController
@RequestMapping(path = "/book")
@CrossOrigin(origins = "*")
public class BookController {
    private final JdbcTemplate jdbcTemplate;

    private final static String updateReviewSql = "SELECT update_user_book_review(?)";

    private final BookChapterRepository bookChapterRepository;

    private final UserBookReviewRepo userBookReviewRepo;

    private final UserBookMarkRepo userBookMarkRepo;

    private final BookRepository bookRepository;

    private final BookUrlRepo bookUrlRepo;

    private final UserBookRatingRepo ratingRepo;

    private final BookHoldingKindRepo holdingKindRepo;

    private final UserBookCommentRepo userBookCommentRepo;

    private final SaveBookUrl saveBookUrl;

    private final BookEntityService bookEntityService;

    @Autowired
    public BookController(BookChapterRepository bookChapterRepository,
                          UserBookReviewRepo userBookReviewRepo,
                          UserBookMarkRepo userBookMarkRepo,
                          BookRepository bookRepository,
                          BookUrlRepo bookUrlRepo,
                          UserBookRatingRepo ratingRepo,
                          BookHoldingKindRepo holdingKindRepo,
                          UserBookCommentRepo userBookCommentRepo,
                          SaveBookUrl saveBookUrl,
                          BookEntityService bookEntityService, JdbcTemplate jdbcTemplate) {
        this.bookChapterRepository = bookChapterRepository;
        this.userBookReviewRepo = userBookReviewRepo;
        this.userBookMarkRepo = userBookMarkRepo;
        this.bookRepository = bookRepository;
        this.bookUrlRepo = bookUrlRepo;
        this.ratingRepo = ratingRepo;
        this.holdingKindRepo = holdingKindRepo;
        this.userBookCommentRepo = userBookCommentRepo;
        this.saveBookUrl = saveBookUrl;
        this.bookEntityService = bookEntityService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 通过输入的书籍的 ISBN 查找对应的书籍的章节信息对象列表
     *
     * @param isbn ： 输入搜索的书籍的 ISBN
     * @return ： 得到的书籍章节信息列表
     */
    @GetMapping(path = "/bookChapter/{isbn}")
    public List<BookChapter> bookChapter(@PathVariable(name = "isbn") Long isbn) {
        return bookChapterRepository.findByIsbn(isbn);
    }

    @GetMapping(path = "/updateBookUrls")
    public HttpStatus updateBookUrls() throws IOException, InterruptedException {
        return saveBookUrl.updateAllBookUrls();
    }

    /**
     * 获取指定书籍的相关评论信息
     * @param isbn ： 当前查找书籍评论的书籍的 isbn 号
     * @return : 得到的用户书籍评论信息对象列表
     */
    @GetMapping(path = "/bookComment/{isbn}")
    public List<UserBookComment> bookComments(@PathVariable(name = "isbn") Long isbn) {
        return userBookCommentRepo.findUserBookCommentByIsbnOrderByCommentDateDesc(isbn);
    }

    /**
     * 获取指定 ISBN 对应的书评信息对象列表
     *
     * @param isbn ： 查找对应的书评的书籍 ISBN 号
     * @return ：得到的最新的书评信息列表
     */
    @GetMapping(path = "/bookReviews/{isbn}")
    public List<UserBookReview> bookReviews(@PathVariable(name = "isbn") Long isbn) {
        List<UserBookReview> bookReviews = userBookReviewRepo
                .findUserBookReviewsByIsbn(isbn);
        for (UserBookReview obj : bookReviews)
            jdbcTemplate.execute(updateReviewSql
                    .replaceAll("\\?", String.valueOf(obj.getBookReviewId()))
            );

        return bookReviews;
    }

    /**
     * 保存一个用户上传的书评
     * @param review ： 用户上传的书评信息对象
     * @return ： 成功
     */
    @PostMapping(path = "/addBookReviews")
    public HttpStatus addBookReviews(@RequestBody UserBookReview review) {
        review.setWriteDate(TimeTools.currentTime());
        userBookReviewRepo.save(review);

        return HttpStatus.OK;
    }

    /**
     * 通过书籍的 ISBN 获取对应的书籍信息
     *
     * @param isbn ： 输入搜索的 ISBN
     * @return ： 得到的书籍信息对象
     */
    @GetMapping(path = "/book/{isbn}")
    public BookDetail findBookByIsbn(@PathVariable(name = "isbn") Long isbn) {
        BookDetail bookDetail = new BookDetail();

        Book book = bookRepository.findBookByIsbn(isbn);
        setBookDetailByBook(bookDetail, book);

        // 设置书籍的评分信息的基本属性
        bookDetail.setOneScoreNum(userBookMarkRepo.countOfOneScoreByIsbn(isbn));
        bookDetail.setTwoScoreNum(userBookMarkRepo.countOfTwoScoreByIsbn(isbn));
        bookDetail.setThreeScoreNum(userBookMarkRepo.countOfThreeScoreByIsbn(isbn));
        bookDetail.setFourScoreNum(userBookMarkRepo.countOfFourScoreByIsbn(isbn));
        bookDetail.setFiveScoreNum(userBookMarkRepo.countOfFiveScoreByIsbn(isbn));

        Double score = calculateBookScore(bookDetail);
        bookDetail.setBookScore(score);
        book.setBookScore(score);
        // 更新图书的评分
        bookRepository.save(book);

        int random = new Random().nextInt(book.getBookImageList().size());
        // 设置书籍的图片地址
        bookDetail.setBookImage(book.getBookImageList().get(random).getImageUrl());

        return bookDetail;
    }

    /**
     * 用户进入 APP 时默认额初始数据界面，
     *  这个界面的数据内容是按照用户的近期访问情况来决定的
     * @param userId ： 访问的用户 ID
     * @return ： 得到的结果对象
     */
    @PostMapping(path = "/defaultBookPage/{userId}")
    public List<BookEntity> defaultBookPage(
            @PathVariable(name = "userId") Long userId) {
        Optional<UserBookRating> userBookRating = ratingRepo.findRecentLikeBook(userId);

        if (userBookRating.isEmpty()) return bookEntityService.findDefaultBooks();

        UserBookRating rating = userBookRating.get();

        Optional<BookHoldingKind> kindOptional = holdingKindRepo
                .findBookHoldingKindByIsbn(rating.getIsbn());

        Double[] relateKind;
        if (kindOptional.isEmpty()) relateKind = Const.mapTable[0];
        else  relateKind = Const.mapTable[kindOptional.get().getBookKindId()];

        List<RelateNode> nodeList = new ArrayList<>();
        for (int i = 1; i < Const.mapTable[0].length; ++i) {
            nodeList.add(new RelateNode(i, relateKind[i]));
        }

        Collections.sort(nodeList);

        return bookEntityService.findBooksByRelateNodes(nodeList);
    }

    /**
     * 保存用户对于书籍的评论信息
     *
     * @param userBookMark ： 要保存的用户评论信息对象
     * @return ： 处理结果， 成功则返回 Http 状态码 200
     */
    @PostMapping(path = "/saveBookMark")
    public HttpStatus saveBookMark(@RequestBody UserBookMark userBookMark) {
        userBookMark.setMarkDate(TimeTools.currentTime());

        UserBookRating rating = new UserBookRating();
        rating.setRate(userBookMark.getScore() * 1.0 / 5);
        rating.setRateTime(TimeTools.currentTime());
        rating.setUserId(userBookMark.getUserId());
        rating.setIsbn(userBookMark.getIsbn());

        ratingRepo.save(rating);

        userBookMarkRepo.save(userBookMark);

        return HttpStatus.OK;
    }

    /**
     * 按照书籍的 ISBN 来查找对应的书籍打分信息列表
     *
     * @param isbn ： 待查找的书籍 ISBN
     * @return ： 得到的书籍的打分信息列表
     */
    @GetMapping(path = "/bookMarks/{isbn}")
    public List<UserBookMark> bookMarks(@PathVariable(name = "isbn") Long isbn) {
        return userBookMarkRepo.findUserBookMarksByIsbn(isbn);
    }

    /**
     * 按照作者 Id 搜索对应的书籍信息
     *
     * @param authorId : 输入的作者 ID 查询参数
     * @return ： 得到的书籍信息列表
     */
    @GetMapping(path = "/searchBookByAuthorId/{authorId}")
    public List<Book> searchBookByAuthorId(@PathVariable(name = "authorId") Long authorId) {
        return bookRepository.getBooksByAuthorId(authorId);
    }

    /**
     * 得到相关书籍页面的列表对象
     *
     * @param start ： 查询的开始位置
     * @param size  ： 本次查询大小
     * @return ： 得到的 BookUrl 对象列表
     */
    @GetMapping(path = "/getBookUrls/{start}/{size}")
    public List<BookUrl> getBookUrls(@PathVariable Long start, @PathVariable Long size) {
        return bookUrlRepo.findBookUrlsByStartAndEnd(start, size);
    }


    // 通过 Book 对象来设置基本的 BookDetail 的基本属性
    private void setBookDetailByBook(@NonNull BookDetail obj,
                                     @NonNull Book book) {
        obj.setIsbn(book.getIsbn());
        obj.setBookName(book.getBookName());
        obj.setIntroduction(book.getIntroduction());
        obj.setBookScore(book.getBookScore());
        obj.setAuthorList(book.getAuthorSet());

        // 获取书籍的出版信息的字符串表示形式
        StringBuilder publisherInfo = new StringBuilder();
        for (BookPublisher bookPublisher : book.getBookPublisherSet()) {
            publisherInfo.append(bookPublisher.getPublisher().getPublisherName())
                    .append(" / ")
                    .append(bookPublisher.getPublishedDate()).append("\n");
        }
        publisherInfo.deleteCharAt(publisherInfo.length() - 1);
        obj.setPublisherInfo(publisherInfo.toString());
    }

    /**
     * 计算该书籍的总平均分
     * @param obj ： 带有相关书籍评分信息的 BookDetail 对象
     * @return ： 得到的书籍的加权平均分
     */
    private Double calculateBookScore(@NonNull BookDetail obj) {
        int oneScore = obj.getOneScoreNum();
        int twoScore = obj.getTwoScoreNum();
        int threeScore = obj.getThreeScoreNum();
        int fourScore = obj.getFourScoreNum();
        int fiveScore = obj.getFiveScoreNum();

        if (oneScore + twoScore + threeScore + fourScore + fiveScore == 0) return 0.0;

        return (double) (oneScore + 2 * twoScore + 3 * threeScore
                + 4 * fourScore + 5 * fiveScore) / (oneScore + twoScore
                + threeScore + fourScore + fiveScore);
    }
}
