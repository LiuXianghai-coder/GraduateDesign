package com.example.bookrecommend.sington;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.threeten.bp.ZoneId;

/**
 * @Author: Administrator
 * @Date: 2021/03/25 11:47
 * @Project: BookRecommend
 **/
@RequiresApi(api = Build.VERSION_CODES.O)
public enum ZoneIdTool {
    DEFAULT_ZONE_ID(ZoneId.of("Asia/Shanghai"));

    private final ZoneId val;

    ZoneIdTool(ZoneId val) {
        this.val = val;
    }

    public ZoneId getVal() {
        return val;
    }
}
