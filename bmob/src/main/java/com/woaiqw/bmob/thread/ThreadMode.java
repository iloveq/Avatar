package com.woaiqw.bmob.thread;

import android.support.annotation.IntDef;

import static com.woaiqw.bmob.thread.ThreadMode.ASYNC;
import static com.woaiqw.bmob.thread.ThreadMode.BACKGROUND;
import static com.woaiqw.bmob.thread.ThreadMode.MAIN;
import static com.woaiqw.bmob.thread.ThreadMode.POSTING;

/**
 * Created by haoran on 2018/8/31.
 */

@IntDef({POSTING, MAIN, BACKGROUND, ASYNC})
public @interface ThreadMode {

    int POSTING = 0x11;


    int MAIN = 0x12;


    int BACKGROUND = 0x13;


    int ASYNC = 0x14;

}