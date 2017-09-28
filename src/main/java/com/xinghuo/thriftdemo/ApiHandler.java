package com.xinghuo.thriftdemo;

import In.ApiService;
import Jiuyan.Thrift.Header.Header;
import com.xinghuo.thriftdemo.face.HandlerFace;
import org.apache.thrift.TException;

/**
 * Created by xinghuo on 2017/9/27.
 */
public class ApiHandler implements ApiService.Iface , HandlerFace {
    protected Header header;
    public  void setHeader(Jiuyan.Thrift.Header.Header t) {
        header = t;
    }

    public String call(String service_name, String method, String params, String request_info) throws TException {
        String tid = "";
        if (header != null ) {
            tid = header.trace_id;
        }
        return "xxxxxxx" + tid;
    }
}
