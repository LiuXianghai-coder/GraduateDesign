package org.graduate.service.controller;

import org.graduate.service.data.UserBookReview;
import org.graduate.service.repository.UserBookReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/3/20
 * @Created : 2021/03/20 - 15:39
 * @Project : service
 */
@Controller
@RequestMapping(path = "/view")
@CrossOrigin(origins = "*")
public class WebViewController {
    private final UserBookReviewRepo bookReviewRepo;

    @Autowired
    public WebViewController(UserBookReviewRepo bookReviewRepo) {
        this.bookReviewRepo = bookReviewRepo;
    }

    /**
     * 获取到对应的书评的 WebView 界面
     * @param reviewId ： 访问的书评 Id
     * @return ： 得到的书评信息的 WebView 界面
     */
    @GetMapping(path = "/bookReview/{reviewId}")
    public String bookReview(@PathVariable(name = "reviewId") Long reviewId, Model model) {
        Optional<UserBookReview> obj = bookReviewRepo.findById(reviewId);
        obj.ifPresent(userBookReview -> model.addAttribute("content", userBookReview.getContent()));

        return "view";
    }
}
