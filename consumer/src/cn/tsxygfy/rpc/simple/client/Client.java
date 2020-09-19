package cn.tsxygfy.rpc.simple.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author feiyang
 */
public class Client {

    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxyObject(Class<?> serviceInterface, InetSocketAddress addr) {

        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                (proxy, method, args) -> {
                    ObjectOutputStream oos = null;
                    ObjectInputStream ois = null;
                    Socket socket = new Socket();
                    try {
                        socket.connect(addr);
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeUTF(serviceInterface.getName());
                        oos.writeUTF(method.getName());
                        oos.writeObject(method.getParameterTypes());
                        oos.writeObject(args);
                        ois = new ObjectInputStream(socket.getInputStream());
                        return ois.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        if (oos != null) {
                            oos.close();
                        }
                        if (ois != null) {
                            ois.close();
                        }
                    }
                });
    }

}
