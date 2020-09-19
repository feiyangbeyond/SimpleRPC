package cn.tsxygfy.rpc.simple;

import cn.tsxygfy.rpc.simple.server.RpcServer;
import cn.tsxygfy.rpc.simple.service.HelloServiceImpl;

import java.io.IOException;

/**
 * @author feiyang
 */
public class RpcServerTest {

    public static void main(String[] args) {
        RpcServer server = RpcServer.getInstance();
        server.setPort(9926);
        server.registry(HelloServiceImpl.class);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("RPC Server Start Error! " + e.getMessage());
        }
    }
}
