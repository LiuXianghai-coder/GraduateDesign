package com.example.bookrecommend.item;

import com.example.bookrecommend.pojo.ShareEntity;

public interface UserShareClick {
    /**
     * 用户点击循环视图中的某个对象时的点击事件监听，该监听应当作出的反应如下：
     *
     *  1. 获得当前点击的视图对象的动态 ID，将它作为参数传递到给下一跳转页面，
     *      这是由于只有在拥有动态 ID 的情况下，才能读取相关的动态内容
     *
     *  2. 保存当前视图的点击位置，以便在之后返回到初始时用户能够继续浏览当前位置的内容
     *
     * @param shareEntity ： 点击的视图实体绑定的动态信息对象
     */
    void userShareClick(ShareEntity shareEntity);
}
