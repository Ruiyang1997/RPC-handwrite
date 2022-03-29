package com.ry.rpc.server;

import com.ry.rpc.codec.Decoder;
import com.ry.rpc.codec.Encoder;
import com.ry.rpc.codec.JSONDecoder;
import com.ry.rpc.codec.JSONEncoder;
import com.ry.rpc.transport.HTTPTransportServer;
import com.ry.rpc.transport.TransportServer;
import lombok.Data;

//server配置
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;
}
