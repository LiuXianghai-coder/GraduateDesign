package com.example.bookrecommend.sington;

import android.annotation.SuppressLint;
import android.view.View;

public class BookReviewView {
    @SuppressLint("StaticFieldLeak")
    private static View reviewView = null;

    /**
     * 获取书评信息列表视图的引用对象，这个方法在主页初始化之前将会返回 null
     *
     * @return ： 得到的书评信息列表视图
     */
    public static View getReviewView() {
        return reviewView;
    }


    /**
     * 设置对于书评信息列表视图的引用
     *
     * 警告：这个方法只有在 BookReviewFragment 中创建视图时使用，
     *     在其它地方使用将会导致整个系统的故障，这对整个系统而言
     *     将会是灾难性的。
     *
     * 设置主页视图对象的引用，该方法只有在当前的书评信息列表视图对象引用为
     *  null 的时候才会设置对应的引用，在其他情况下将会忽略这个对象
     *  的引用设置
     * @param reviewView ： 设置的书评信息列表视图
     */
    public static void setReviewView(View reviewView) {
        BookReviewView.reviewView = reviewView;
    }
}
