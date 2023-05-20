package com.ytrue.rpc.transport;

import com.ytrue.rpc.protocol.RpcRequest;
import com.ytrue.rpc.protocol.RpcResponse;
import com.ytrue.rpc.register.HostAndPort;
import com.ytrue.rpc.service.OrderService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ytrue
 * @date 2023-05-19 19:42
 * @description NettyTransport
 */
@Slf4j
public class NettyTransport implements Transport {

    private Bootstrap bootstrap;

    private EventLoopGroup worker;

    public NettyTransport() {
        this(1);
    }

    public NettyTransport(int workerThreads) {
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup(workerThreads);
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }

    @Override
    public RpcResponse invoke(HostAndPort hostAndPort, RpcRequest request) throws Exception {

        RpcClientChannelInitializer rpcClientChannelInitializer = new RpcClientChannelInitializer(request);
        bootstrap.handler(rpcClientChannelInitializer);

        ChannelFuture channelFuture = bootstrap.connect(hostAndPort.getHostName(), hostAndPort.getPort()).sync();
        channelFuture.channel().closeFuture().sync();
        return rpcClientChannelInitializer.getResponse();
    }

    @Override
    public void close() {
        worker.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {

        NettyTransport nettyTransport = new NettyTransport();

        HostAndPort hostAndPort = new HostAndPort();
        hostAndPort.setHostName("127.0.0.1");
        hostAndPort.setPort(1010);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setTargetInterface(OrderService.class);
        rpcRequest.setArgs(new Object[]{"yangyi"});
        rpcRequest.setMethodName("test01");
        rpcRequest.setParameterTypes(new Class[]{String.class});

        System.out.println(nettyTransport.invoke(hostAndPort, rpcRequest));
        nettyTransport.close();

    }
}
