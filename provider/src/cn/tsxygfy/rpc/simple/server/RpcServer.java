package cn.tsxygfy.rpc.simple.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author feiyang
 */
@SuppressWarnings("all")
public class RpcServer implements Server {

    private static RpcServer rpcServer = new RpcServer();

    private final HashMap<String, Class<?>> SERVICES_REGISTER = new HashMap<>();

    private boolean isRunning = false;

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private int port;

    private RpcServer() {
    }

    public static RpcServer getInstance() {
        return rpcServer;
    }

    @Override
    public void start() throws IOException {

        ServerSocket server = new ServerSocket(port);
        isRunning = true;
        System.out.println("Start server @" + server.getInetAddress().getHostAddress() + ":" + port);
        while (true) {
            // build socket
            Socket socket = server.accept();

            executor.execute(() -> {
                ObjectInputStream ois = null;
                ObjectOutputStream oos = null;
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    // get value from stream
                    String clazzInterface = ois.readUTF();
                    String method = ois.readUTF();
                    Class<?>[] paramTypes = (Class<?>[]) ois.readObject();
                    Object[] arguments = (Object[]) ois.readObject();
                    // get remote
                    Class<?> service = SERVICES_REGISTER.get(clazzInterface);
                    Method serviceMethod = service.getMethod(method, paramTypes);
                    // execute method
                    Object result = serviceMethod.invoke(service.newInstance(), arguments);
                    // write return value
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(result);
                    System.out.println(clazzInterface + "#" + method + " Return value is " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // close stream
                    try {
                        if (ois != null) {
                            ois.close();
                        }
                        if (oos != null) {
                            oos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void registry(Class<?> service) {
        // service implements one interface
        SERVICES_REGISTER.put(service.getInterfaces()[0].getName(), service);
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
