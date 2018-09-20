package com.woaiqw.avatar.model;

import java.util.List;

/**
 * Created by haoran on 2018/9/20.
 */
public class Request {

    /**
     * process : com.woaigmz.avatar
     * className : com.woaigmz.avatar.MainActivity
     * methods : [{"methodName":"changeText","methodParams":"hhhh","tag":"change_text"},{"methodName":"changeColor","methodParams":"#FF0000","tag":"change_color"}]
     */

    public String process;
    public String className;
    public List<MethodsBean> methods;


    public static class MethodsBean {
        /**
         * methodName : changeText
         * methodParams : hhhh
         * tag : change_text
         */

        public String methodName;
        public String methodParams;
        public String tag;

    }
}
