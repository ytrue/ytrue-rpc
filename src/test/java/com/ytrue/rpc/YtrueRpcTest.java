package com.ytrue.rpc;

import com.ytrue.rpc.server.RpcServerProvider;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author ytrue
 * @date 2023-05-19 12:56
 * @description YtrueRpcTest
 */
public class YtrueRpcTest {

    @Test
    public void test01() throws IOException, InterruptedException {
        RpcServerProvider rpcServerProvider = new RpcServerProvider(null,null);
        rpcServerProvider.startServer();

        System.in.read();
    }
}
