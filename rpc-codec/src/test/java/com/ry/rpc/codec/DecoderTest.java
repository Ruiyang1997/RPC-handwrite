package com.ry.rpc.codec;

import org.junit.Test;

import static org.junit.Assert.*;

public class DecoderTest {

    @Test
    public void decoder() {
        Encoder encoder = new JSONEncoder();

        TestBean bean = new TestBean();
        bean.setName("RUI Yang");
        bean.setAge(18);

        byte[] bytes = encoder.encode(bean);

        Decoder decoder = new JSONDecoder();
        TestBean bean2 = decoder.decode(bytes, TestBean.class);

        assertEquals(bean.getName(), bean2.getName());
        assertEquals(bean.getAge(), bean2.getAge());
    }
}