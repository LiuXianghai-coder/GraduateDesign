package com.example.bookrecommend.sington;

/**
 * @Author: Administrator
 * @Date: 2021/03/23 13:14
 * @Project: BookRecommend
 **/
public enum SaveTime {
    DEFAULT(7*24*60*60*1000L);

    private final Long saveTime;

    SaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }

    public Long getSaveTime() {
        return saveTime;
    }
}
