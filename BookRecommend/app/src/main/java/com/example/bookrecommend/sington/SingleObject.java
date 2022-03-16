package com.example.bookrecommend.sington;

import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.UserBookCollection;
import com.example.bookrecommend.pojo.UserBookStar;

/**
 * @Author: Administrator
 * @Date: 2021/03/26 19:27
 * @Project: BookRecommend
 **/
public final class SingleObject {
    private final static UserInfoEntity userInfoEntity = new UserInfoEntity();

    private final static UserBookStar userBookStar = new UserBookStar();

    private final static UserBookCollection userBookCollection = new UserBookCollection();

    public static UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
    }

    public static UserBookStar getUserBookStar() {return userBookStar;}

    public static UserBookCollection getUserBookCollection() {return userBookCollection;}
}
