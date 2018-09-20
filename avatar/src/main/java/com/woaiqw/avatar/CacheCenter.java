package com.woaiqw.avatar;

import java.util.HashMap;

/**
 * Created by haoran on 2018/9/20.
 * 存在于每个内存的进程里
 */
public class CacheCenter {

    private HashMap<String, Object> map;

    private CacheCenter() {
        map = new HashMap<>(12);
    }

    private static class Holder {
        private static final CacheCenter IN = new CacheCenter();
    }

    public static CacheCenter getInstance() {
        return Holder.IN;
    }

    /****** operator *****************************************************************************************************/

    public void cache(String className, Object register) {
        map.put(className, register);
    }

    public Object get(String source) {
        return map.get(source);
    }

    public void dispose() {
        map.clear();
    }

    public void remove(String className) {
        map.remove(className);
    }

    public HashMap<String, Object> getSubsciebesMap() {
        return map;
    }


}
