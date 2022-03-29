package com.ry.rpc.transport;
//1启动监听 2接受请求 3关闭监听
public interface TransportServer {
    void init(int port, RequestHandler handler);
    void start();


    void stop();
}
