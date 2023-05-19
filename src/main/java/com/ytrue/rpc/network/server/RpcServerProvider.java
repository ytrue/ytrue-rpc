package com.ytrue.rpc.network.server;

import com.ytrue.rpc.register.HostAndPort;
import com.ytrue.rpc.register.Registry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @date 2023-05-19 12:54
 * @description RpcServerProvider
 */
public class RpcServerProvider {

    /**
     * 端口号
     */
    private int port;

    /**
     * boos
     */
    private EventLoopGroup eventLoopGroupBoss;

    /**
     * work
     */
    private EventLoopGroup eventLoopGroupWorker;

    /**
     * Netty的编解码器 内置Handler通过这个线程组服务
     */
    private EventLoopGroup eventLoopGroupHandler;

    /**
     * service
     */
    private EventLoopGroup eventLoopGroupService;

    /**
     * word thread number
     */
    private int workerThreads;

    /**
     * handler thread number
     */
    private int handlerThreads;

    /**
     * service thread number
     */
    private int serviceThreads;

    /**
     * register
     */
    private Registry registry;

    /**
     * serverBootstrap
     */
    private ServerBootstrap serverBootstrap;

    /**
     * key = 服务的接口, value = 服务对象
     */
    private Map<String, Object> exposeBeans;

    /**
     * 启动状态
     */
    private boolean isStarted;


    public RpcServerProvider(Registry registry, Map<String, Object> exposeBeans) {
        this(9992, 1, 1, 1, registry, exposeBeans);
    }

    public RpcServerProvider(int port, Registry registry, Map<String, Object> exposeBeans) {
        this(port, 1, 1, 1, registry, exposeBeans);
    }

    public RpcServerProvider(int port, int workerThreads, int handlerThreads, int serviceThreads, Registry registry, Map<String, Object> exposeBeans) {
        // 设置端口
        this.port = port;
        // 对应的工作线程
        this.workerThreads = workerThreads;
        this.handlerThreads = handlerThreads;
        this.serviceThreads = serviceThreads;

        // 线程组
        this.eventLoopGroupBoss = new NioEventLoopGroup(1);
        this.eventLoopGroupWorker = new NioEventLoopGroup(this.workerThreads);
        this.eventLoopGroupHandler = new DefaultEventLoopGroup(this.handlerThreads);
        this.eventLoopGroupService = new DefaultEventLoopGroup(this.serviceThreads);

        // 注册器
        this.registry = registry;
        // 关系衍射
        this.exposeBeans = exposeBeans;
        // netty server
        this.serverBootstrap = new ServerBootstrap();
    }


    /**
     * 启动服务
     *
     * @throws InterruptedException
     * @throws UnknownHostException
     */
    public void startServer() throws InterruptedException, UnknownHostException {
        // 校验，防止多次启动，出现端口占用
        if (isStarted) {
            throw new RuntimeException("server is already started....");
        }

        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWorker);
        serverBootstrap.childHandler(new RpcServerProviderInitializer());

        final ChannelFuture channelFuture = serverBootstrap.bind(port);

        // GenericFutureListener
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                //2服务注册功能
                registerServices(InetAddress.getLocalHost().getHostAddress(), port, exposeBeans, registry);
                isStarted = true;

                //监听关闭
                Channel channel = channelFuture.channel();
                ChannelFuture closeFuture = channel.closeFuture();
                // GenericFutureListener
                closeFuture.addListener(f -> {
                    if (f.isSuccess()) {
                        stopServer();
                    }
                });
            }
        });

        // 注册非正常关闭
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));
    }

    /**
     * 关闭服务 同时释放资源
     */
    private void stopServer() {
        eventLoopGroupBoss.shutdownGracefully();
        eventLoopGroupWorker.shutdownGracefully();
        eventLoopGroupHandler.shutdownGracefully();
        eventLoopGroupService.shutdownGracefully();
    }

    /**
     * 服务注册
     *
     * @param hostAddress
     * @param port
     * @param exposeBeans
     * @param registry
     */
    private void registerServices(String hostAddress, int port, Map<String, Object> exposeBeans, Registry registry) {
        //1 通过exposeBeans中获得所有的服务的对象
        Set<String> keySet = exposeBeans.keySet();

        //2 遍历这些对象通过registry进行注册
        HostAndPort hostAndPort = new HostAndPort(hostAddress, port);
        for (String targetInterface : keySet) {
            registry.registerService(targetInterface, hostAndPort);
        }
    }
}
