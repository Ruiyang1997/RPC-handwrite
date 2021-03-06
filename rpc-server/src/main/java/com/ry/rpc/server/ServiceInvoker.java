package com.ry.rpc.server;

import com.ry.rpc.Request;
import com.ry.rpc.common.utils.ReflectionUtils;
//调用具体服务
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(
                service.getTarget(),
                service.getMethod(),
                request.getParameters()
        );
    }
}
