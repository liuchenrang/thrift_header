package com.xinghuo.thriftdemo;

import In.ApiService;
import org.apache.thrift.ProcessFunction;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;

import static javafx.application.Platform.exit;

/**
 * Created by xinghuo on 2017/9/27.
 */
public class ThriftServer {
    public static void main(String... args) {
        String portt = "9010";
        try {
            ApiHandler handler = new ApiHandler();
            ApiService.Processor asyncProcessor = new ApiService.Processor(handler);

//            try {
//                Field fs = asyncProcessor.getClass().getSuperclass().getDeclaredField("processMap");
//                fs.setAccessible(true);
//                System.out.println(fs.get(asyncProcessor) instanceof Map);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }

            try {
                Field fs = asyncProcessor.getClass().getSuperclass().getDeclaredField("iface");
                fs.setAccessible(true);
                System.out.println(fs.get(asyncProcessor));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            ProcessorProxy processorProxy = new ProcessorProxy();
            processorProxy.setProcess(asyncProcessor);
            Integer port = Integer.valueOf(portt);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("0.0.0.0", port);
            TServerSocket.ServerSocketTransportArgs ssta = new TServerSocket.ServerSocketTransportArgs();
            ssta.bindAddr(inetSocketAddress);
            Integer backlog = Integer.valueOf(128);
            ssta.backlog(backlog);
            TServerTransport serverTransport = new TServerSocket(ssta);
            TThreadPoolServer.Args trArgs = new TThreadPoolServer.Args(serverTransport);

            trArgs.maxWorkerThreads(Integer.valueOf(5));
            trArgs.minWorkerThreads(Integer.valueOf(1));
            trArgs.stopTimeoutVal = Integer.valueOf(10);
            trArgs.processor(processorProxy);
            // 使用二进制来编码应用层的数据
            trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
            // 使用普通的socket来传输数据
//            trArgs.transportFactory(new TTransportFactory());
            trArgs.transportFactory(new TFramedTransport.Factory());
            TThreadPoolServer server = new TThreadPoolServer(trArgs);
//					ThriftServerHolder.setThriftServer(server);
            server.serve();
            System.out.println("THRIFT START AND PORT IS " + String.valueOf(port));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
