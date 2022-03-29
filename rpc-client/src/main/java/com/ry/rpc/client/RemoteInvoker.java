package com.ry.rpc.client;

import com.ry.rpc.Request;
import com.ry.rpc.Response;
import com.ry.rpc.ServiceDescriptor;
import com.ry.rpc.codec.Decoder;
import com.ry.rpc.codec.Encoder;
import com.ry.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//调用远程服务的代理类
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz,
                  Encoder encoder,
                  Decoder decoder,
                  TransportSelector selector){
        this.clazz = clazz;
        this.decoder = decoder;
        this.encoder = encoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        Response resp = invokeRemote(request);
        if(resp == null || resp.getCode() != 0){
            throw new IllegalStateException("fail to invoke remote:" + resp);
        }

        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        TransportClient client = null;
        Response response = null;
        try {
            client = selector.select();
            byte[] bytes = encoder.encode(request);
            InputStream in = client.write(new ByteArrayInputStream(bytes));
            byte[] inBytes = IOUtils.readFully(in, in.available());
            response = decoder.decode(inBytes, Response.class);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            response.setCode(1);
            response.setMessage("RpcClient got error:" + e.getClass() + ":" + e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }
        return response;
    }
}
