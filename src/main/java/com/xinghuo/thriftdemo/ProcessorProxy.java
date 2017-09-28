package com.xinghuo.thriftdemo;

import Jiuyan.Thrift.Header.Header;
import Jiuyan.Thrift.Header.RequestHeader;
import com.sun.tools.internal.ws.processor.model.HeaderFault;
import com.xinghuo.thriftdemo.face.HandlerFace;
import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by xinghuo on 2017/9/27.
 */
public class ProcessorProxy implements TProcessor {
    protected TProcessor processor;
    protected Header header;
    protected HandlerFace handler;
    protected Field fprocess;
    protected Field fface;

    public void setProcess(TProcessor process) {
        processor = process;
        try {
            fprocess = processor.getClass().getSuperclass().getDeclaredField("processMap");
            fprocess.setAccessible(true);

            fface = processor.getClass().getSuperclass().getDeclaredField("iface");
            fface.setAccessible(true);
            try {
                handler = (HandlerFace) fface.get(processor);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public boolean process(TProtocol in, TProtocol out) throws TException {

        TMessage msg = in.readMessageBegin();
        if (msg.name.equals("_header")) {
            process_header(in, out);
            return process(in, out);
        }
        try {

            Map processMap = (Map) fprocess.get(processor);
            ProcessFunction fn = (ProcessFunction) processMap.get(msg.name);
            if (fn == null) {
                TProtocolUtil.skip(in, TType.STRUCT);
                in.readMessageEnd();
                TApplicationException x = new TApplicationException(TApplicationException.UNKNOWN_METHOD, "Invalid method name: '" + msg.name + "'");
                out.writeMessageBegin(new TMessage(msg.name, TMessageType.EXCEPTION, msg.seqid));
                x.write(out);
                out.writeMessageEnd();
                out.getTransport().flush();
                return true;
            }
            fn.process(msg.seqid, in, out,  fface.get(processor));
            handler.setHeader(null);

            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void process_header(TProtocol in, TProtocol out) {
        RequestHeader._header_args args = new RequestHeader._header_args();
        try {
            args.read(in);
            header = args.header;
            in.readMessageEnd();
            if (handler instanceof HandlerFace) {
                handler.setHeader(header);
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }

}
