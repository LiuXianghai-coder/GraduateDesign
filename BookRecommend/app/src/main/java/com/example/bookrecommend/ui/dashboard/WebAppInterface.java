package com.example.bookrecommend.ui.dashboard;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @Author : LiuXianghai
 * @Date : 2021/03/01 15:12
 * @Product :  BookRecommend
 */
public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void addImage(ImageView imageView) {
    }
}
