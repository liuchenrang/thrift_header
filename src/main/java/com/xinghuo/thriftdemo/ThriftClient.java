package com.xinghuo.thriftdemo;

import In.ApiService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.net.InetSocketAddress;

/**
 * Created by xinghuo on 2017/9/27.
 */
public class ThriftClient {
    public static void main(String ...args){
        String portt = "9010";
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
