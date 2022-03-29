package com.ry.rpc.client;

import com.ry.rpc.Peer;
import com.ry.rpc.transport.TransportClient;

import java.util.List;

public interface TransportSelector {
    void init(List<Peer> peers,
              int count,
              Class<? extends TransportClient> clazz);

    TransportClient select();

    void release(TransportClient client);

    void close();
}
