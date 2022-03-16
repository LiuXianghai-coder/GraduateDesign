package com.example.bookrecommend.sington;

/**
 * @Author: Administrator
 * @Date: 2021/03/25 11:49
 * @Project: BookRecommend
 **/
public enum DateFormatTool {
    DEFAULT_DATE_FORMAT("yyyy-MM-dd HH:mm:ss");

    private final String val;

    DateFormatTool(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
