package cn.tsxygfy.rpc.simple;

import cn.tsxygfy.rpc.simple.client.Client;
import cn.tsxygfy.rpc.simple.service.HelloService;

import java.net.InetSocketAddress;

/**
 * @author feiyang
 */
public class RpcClientTest {

    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> clazz = Class.forName("cn.tsxygfy.rpc.simple.service.HelloService");
        HelloService helloService = Client.getRemoteProxyObject(clazz, new InetSocketAddress(9926));
        String message = helloService.sayHi("Anni");
        System.out.println(message);
    }

}
