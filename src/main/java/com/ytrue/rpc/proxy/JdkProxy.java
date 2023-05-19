package com.ytrue.rpc.proxy;

import com.ytrue.rpc.protocol.RpcRequest;
import com.ytrue.rpc.utils.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author ytrue
 * @date 2023-05-19 16:36
 * @description JdkProxy
 */
public class JdkProxy {

    /**
     * 获取代理
     *
     * @param interfaceClass
     * @param request
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getProxy(Class<T> interfaceClass, RpcRequest request) throws Exception {

        InvocationHandler handler = new JdkInvocationHandler(request);
        ClassLoader classLoader = ClassLoaderUtils.getCurrentClassLoader();
        T result = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, handler);
        return result;
    }
}
