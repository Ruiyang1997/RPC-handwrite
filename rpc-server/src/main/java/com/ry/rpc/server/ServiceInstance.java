package com.ry.rpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
//表示具体一个服务
@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}
