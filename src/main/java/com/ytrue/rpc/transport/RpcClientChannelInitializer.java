package com.ytrue.rpc.transport;

import com.ytrue.rpc.codec.RpcMessageToMessageCodec;
import com.ytrue.rpc.protocol.RpcRequest;
import com.ytrue.rpc.protocol.RpcResponse;
import com.ytrue.rpc.serializar.HessianSerializer;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ytrue
 * @date 2023-05-19 19:43
 * @description RpcClientChannelInitializer
 */
@Slf4j
public class RpcClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private RpcRequest request;

    @Setter
    @Getter
    private RpcResponse response;

    public RpcClientChannelInitializer(RpcRequest request) {
        this.request = request;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 6, 4, 0, 0));
        pipeline.addLast(new LoggingHandler());
        pipeline.addLast(new RpcMessageToMessageCodec(new HessianSerializer()));
        pipeline.addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                log.debug("发送数据...{} ", request);
                ChannelFuture channelFuture = ctx.writeAndFlush(request);
                channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        });
    }
}
