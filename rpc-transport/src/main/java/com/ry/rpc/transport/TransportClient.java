package com.ry.rpc.transport;

import com.ry.rpc.Peer;

import java.io.InputStream;

//1创建连接 2发送数据 并等待响应 3关闭连接
public interface TransportClient {
    void connect(Peer peer);

    InputStream write(InputStream data);

    void close();

}
