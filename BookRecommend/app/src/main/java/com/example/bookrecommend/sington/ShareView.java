package com.example.bookrecommend.sington;

import android.annotation.SuppressLint;
import android.view.View;

public class ShareView {
    /**
     * 一个视图对象的引用，该视图对象引用为对应的用户动态的初始页面视图，
     *  这么做的目的是为了在循环视图中找到导航的基础视图，
     *  由于数据绑定的循环视图并不支持导航，因此，这个对象的引用是必须的
     *
     * 警告：这个视图只能在用户进入用户动态初始界面的时候即创建它，
     *  其它情况下，尝试对此视图进行设置将会导致极其严重的后果。
     *
     *  该对象的引用只能初始化一次，在第一次初始化之后，
     *      任何尝试设置该引用的操作都会被忽略。
     *
     */
    @SuppressLint("StaticFieldLeak")
    private static View shareView = null;

    /**
     * 或得用户动态信息初始界面的视图，这个视图在未初始化时将会返回 null
     *
     * 不用过于担心 null 的问题，因为这个方法只有在用户需要进行动态之间的导航时才会使用
     *  而此时的视图引用应当是已经初始化了的。
     *
     * @return ： 获得用户动态初始界面的视图对象
     */
    public static View getShareView() {
        return shareView;
    }

    /**
     * 设置用户动态界面视图对象的引用，只有在第一次进入用户动态主界面的时候才会设置，
     *  其它情况下，千万不要随意地调用这个方法，
     *  否则，你的周末可能就没了 ：）
     *
     * @param view ： 第一次进入用户主界面的视图对象
     */
    public static void setShareView(View view) {
        if (null != shareView) return;

        shareView = view;
    }
}
