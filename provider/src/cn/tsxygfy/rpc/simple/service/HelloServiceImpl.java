package cn.tsxygfy.rpc.simple.service;

/**
 * @author feiyang
 */
public class HelloServiceImpl implements HelloService {
	@Override
	public String sayHi(String name) {
		return "Hi, " + name;
	}
}
