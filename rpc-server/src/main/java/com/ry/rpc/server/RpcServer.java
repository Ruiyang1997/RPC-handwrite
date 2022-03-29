package com.ry.rpc.server;

import com.ry.rpc.Request;
import com.ry.rpc.Response;
import com.ry.rpc.codec.Decoder;
import com.ry.rpc.codec.Encoder;
import com.ry.rpc.common.utils.ReflectionUtils;
import com.ry.rpc.transport.RequestHandler;
import com.ry.rpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;

    public RpcServer() {
        this(new RpcServerConfig());
    }

    public RpcServer(RpcServerConfig config){
        this.config = config;
        //net
        this.net = ReflectionUtils.newInstance(
                config.getTransportClass());
        this.net.init(config.getPort(), this.handler);
        //encode
        this.encoder = ReflectionUtils.newInstance(
                config.getEncoderClass());
        //decode
        this.decoder = ReflectionUtils.newInstance(
                config.getDecoderClass());
        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    public <T> void register(Class<T> interfaceClass, T bean){
        serviceManager.register(interfaceClass, bean);
    }

    public void start(){
        this.net.start();
    }

    public void stop(){
        this.net.stop();
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response();

            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request:{}", request);

                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoke(sis, request);
                resp.setData(ret);

            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error:"
                                + e.getClass().getName()
                                + ":" + e.getMessage());
            } finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);
                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };
}
