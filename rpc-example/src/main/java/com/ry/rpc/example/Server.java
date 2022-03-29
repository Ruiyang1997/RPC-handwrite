package com.ry.rpc.example;

import com.ry.rpc.server.RpcServer;
import com.ry.rpc.server.RpcServerConfig;

public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(new RpcServerConfig());
        server.register(CalcService.class, new CalcServiceImpl());
        server.start();
    }
}
