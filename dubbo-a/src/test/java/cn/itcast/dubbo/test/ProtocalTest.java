package cn.itcast.dubbo.test;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol;

public class ProtocalTest {

	public static void main(String[] args) {
		testAdaptiveExtension();
	}
	
	public static void testAdaptiveExtension(){
		ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
	}
	
	//测试自定义protocol的wrapper扩展
	public static void testExtloader() {
		Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class)
				.getExtension("registry");
		System.out.println(protocol instanceof DubboProtocol);
		System.out.println(ExtensionLoader.getExtensionLoader(Protocol.class)
				.getSupportedExtensions());

		System.out.println(protocol.getDefaultPort());
	}

}
