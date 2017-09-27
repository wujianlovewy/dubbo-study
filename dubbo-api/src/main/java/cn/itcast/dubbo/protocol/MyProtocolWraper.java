package cn.itcast.dubbo.protocol;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 自定义protocol包装类
 * @author jwu
 * dubbo源码服务发布:http://blog.csdn.net/qq418517226/article/details/51818769
 */
public class MyProtocolWraper implements Protocol{

	private final Protocol protocol;
	
	public MyProtocolWraper(Protocol protocol){
		 if (protocol == null) {
	            throw new IllegalArgumentException("protocol == null");
	     }
	     this.protocol = protocol;
	     System.out.println("构造我自己的MyProtocolWraper");
	}
	
	@Override
	public int getDefaultPort() {
		System.out.println("调用MyProtocolWraper的方法getDefaultPort()");
		return this.protocol.getDefaultPort();
	}

	@Override
	@Adaptive
	public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
		System.out.println("调用MyProtocolWraper的方法export()");
		return protocol.export(invoker);
	}

	@Override
	@Adaptive
	public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
		System.out.println("调用MyProtocolWraper的方法refer()");
		return protocol.refer(type, url);
	}

	@Override
	public void destroy() {
		System.out.println("调用MyProtocolWraper的方法destroy()");
		this.protocol.destroy();
	}

}
