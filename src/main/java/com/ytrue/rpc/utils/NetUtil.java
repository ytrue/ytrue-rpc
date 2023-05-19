package com.ytrue.rpc.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @author ytrue
 * @date 2023-05-19 16:41
 * @description NetUtil
 */
public class NetUtil {

    private static final String LOCALHOST = "localhost";

    /**
     * 端口是否被使用
     *
     * @param port
     * @return
     */
    public static boolean isPortUsing(int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket(LOCALHOST, port);
            socket.close();
            flag = true;
        } catch (IOException ignored) {

        }
        return flag;
    }

    /**
     * 获取地址
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
