package com.example.bookrecommend.item;

import androidx.annotation.NonNull;

import com.example.bookrecommend.pojo.UserBookReview;

public interface BookReviewClick {
    /**
     * 这个方法的作用是为了给每个用户写的书评对像添加对应的监听，实现相关的跳转;
     *
     * 这个跳转需要相关的书评 id 参数，因此需要 UserBookReview 对象作为参数
     *
     * @param review ： 当前点击的 UserBookReview 对象
     */
    void bookReviewClick(@NonNull UserBookReview review);
}
