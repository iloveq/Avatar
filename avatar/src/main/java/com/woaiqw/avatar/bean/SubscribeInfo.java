package com.woaiqw.avatar.bean;

/**
 * Created by haoran on 2018/9/20.
 */
public class SubscribeInfo {


    /**
     * process : com.woaigmz.avatar
     * className : com.woaigmz.avatar.MainActivity
     * methodName : changeText
     * thread : main
     * tag : change_text
     * methodParam :  hello
     */

    public String process;
    public String className;
    public String methodName;
    public String thread;
    public String tag;
    public String methodParam;

    @Override
    public String toString() {
        return "SubscribeInfo{" +
                "process='" + process + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", thread='" + thread + '\'' +
                ", tag='" + tag + '\'' +
                ", methodParam='" + methodParam + '\'' +
                '}';
    }
}
