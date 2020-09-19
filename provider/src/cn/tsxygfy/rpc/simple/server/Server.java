package cn.tsxygfy.rpc.simple.server;

import java.io.IOException;

/**
 * @author feiyang
 */
public interface Server {

	/**
	 * 服务端启动
	 *
	 * @throws IOException
	 */
	void start() throws IOException;

	/**
	 * 服务端停止
	 */
	void stop();

	/**
	 * 注册一个服务
	 *
	 * @param service Class
	 */
	void registry(Class<?> service);

}
