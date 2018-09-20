package com.woaiqw.avatar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by haoran on 2018/8/31.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    String DEFAULT = "avatar";

    String tag() default DEFAULT;

    ThreadMode thread() default ThreadMode.MAIN;

}
