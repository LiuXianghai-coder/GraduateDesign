package com.example.bookrecommend.item;

import com.example.bookrecommend.pojo.BookEntity;

public interface BookItemClick {
    /**
     * 当用户点击对应的书籍的时候出现的事件，
     * 这里的接口要求当用户点击对应的书籍时可以跳转到相关的书籍详情页面，
     * 同时，这里在点击时应当能够记录用户对于当前书籍的浏览信息，以便于之后的对用户的兴趣的预测
     *
     * @param obj : 当前点击的书籍实体对象
     */
    void bookItemClick(BookEntity obj);
}
