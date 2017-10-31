package cn.edu.wj.rpc.test;

import cn.edu.wj.rpc.demo.RpcService;

@RpcService("helloService")
public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHi(String msg) {
		return "Hello! "+msg;
	}

}
