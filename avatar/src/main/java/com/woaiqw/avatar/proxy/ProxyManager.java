package com.woaiqw.avatar.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by haoran on 2018/9/20.
 */
public class ProxyManager {

    private ProxyManager() {
    }

    private static class Holder {
        private static final ProxyManager IN = new ProxyManager();
    }

    public static ProxyManager getInstance() {
        return Holder.IN;
    }



    /**
     * getProxy
     *
     * @param clazz realObj
     * @return proxy
     */
    private static <T> T getProxy(Class<T> clazz) {
        T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RPCProxyHandler(clazz));
        return proxy;
    }

}
