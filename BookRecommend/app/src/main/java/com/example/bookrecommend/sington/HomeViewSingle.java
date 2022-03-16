package com.example.bookrecommend.sington;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;

public class HomeViewSingle {
    /**
     * 指向图书信息主页的视图对象，这个对象的存在应当只有一个，因此它是一个单例
     *
     * 这个试图是为了保存对主页视图的引用，这是由于：
     *  1. 有的地方导航需要使用到主页面视图，但是没有办法得到它
     */
    @SuppressLint("StaticFieldLeak")
    private static View homeView = null;

    /**
     * 设置对于主页试图的引用
     *
     * 警告：这个方法只有在 HomeFragment 中创建视图时使用，
     *     在其它地方使用将会导致整个系统的故障，这对整个系统而言
     *     将会是灾难性的。
     *
     * 设置主页视图对象的引用，该方法只有在当前的主页视图对象引用为
     *  null 的时候才会设置对应的引用，在其他情况下将会忽略这个对象
     *  的引用设置
     * @param view ： 设置的主页试图对象
     */
    public static void setHomeView(@NonNull View view) {
        if (null != homeView) return;
        homeView = view;
    }

    /**
     * 获取主页视图的引用对象，这个方法在主页初始化之前将会返回 null
     *
     * @return ： 得到的主页视图
     */
    public static View getHomeView() {
        return homeView;
    }
}
