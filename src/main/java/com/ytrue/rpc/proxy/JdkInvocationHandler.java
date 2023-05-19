package com.ytrue.rpc.proxy;

import com.ytrue.rpc.protocol.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2023-05-19 16:36
 * @description JdkInvocationHandler
 */
public class JdkInvocationHandler implements InvocationHandler {

    public JdkInvocationHandler(RpcRequest request) {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO 待完善
        return null;
    }
}
