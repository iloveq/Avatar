package com.woaiqw.avatar;

import com.woaiqw.avatar.thread.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by haoran on 2018/8/31.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Subscribe {

    Tag[] tags() default {};

    int thread() default ThreadMode.MAIN;

}
