package com.ytrue.rpc.network.server;

import com.ytrue.rpc.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * @author ytrue
 * @date 2023-05-19 16:50
 * @description RpcRequestInboundHandler
 */
public class RpcRequestInboundHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> exposeBean;

    public RpcRequestInboundHandler(Map<String, Object> exposeBean) {
        this.exposeBean = exposeBean;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

        // TODO 待开发

        System.out.println(exposeBean);
    }
}
