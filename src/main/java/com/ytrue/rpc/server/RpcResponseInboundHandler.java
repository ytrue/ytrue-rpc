package com.ytrue.rpc.server;

import com.ytrue.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ytrue
 * @date 2023-05-19 20:16
 * @description RpcResponseInboundHandler
 */
public class RpcResponseInboundHandler  extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        // TODO 发送响应
    }
}
