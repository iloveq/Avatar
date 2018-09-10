// IAvatarAidlInterface.aidl
package com.woaiqw.avatar;

// Declare any non-default types here with import statements

interface IAvatarAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void post(String tag,String content);

    void register(String className,String subscribe);

    void unregister(String className);

}
