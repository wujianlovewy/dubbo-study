package cn.edu.wj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.service.UserService;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class DubboTest {
	
	public static void main(String[] args) throws Exception {
		testUserId();
	}
	
	/**
	 * 测试userID生成
	 */
	public static void testUserId() throws Exception{
		ApplicationConfig config = new ApplicationConfig();
		config.setName("dubbo");
		
		ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<UserService>();
		referenceConfig.setInterface(UserService.class);
		referenceConfig.setApplication(config);
		referenceConfig.setTimeout(60000);
		
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("dubbo");
		protocolConfig.setDispatcher("direct");
		
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress("zookeeper://nbmszk1:2117?backup=nbmszk2:2118,nbmszk3:2119");
		
		referenceConfig.setRegistry(registryConfig);
		final UserService userService = referenceConfig.get();
		System.out.println(userService);
		int count = 100;
		int m = 100;
		int countLatch = count * m;
		long startTime = System.currentTimeMillis();
		final CountDownLatch end = new CountDownLatch(countLatch);
		for(int n=0;n<m;n++){
			final CountDownLatch start = new CountDownLatch(1);
			for(int i=0;i<count;i++){
				new Thread(){
					@Override
					public void run(){
						try {
							start.await();
							System.out.println(userService.generateId());
							end.countDown();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
			start.countDown();
		}
		end.await();
		System.out.println(System.currentTimeMillis()-startTime);
	}
	
	/**
	 * 集群环境下dubbo才会调用负载策略
	 * @param args
	 */
	public static void testLoadBalance(){
		ApplicationConfig config = new ApplicationConfig();
		config.setName("dubbo");
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();
		MethodConfig method = new MethodConfig();
		method.setLoadbalance("myload");
		method.setName("updateProductStock");
		methods.add(method);
		
		ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<UserService>();
		referenceConfig.setInterface(UserService.class);
		referenceConfig.setApplication(config);
		referenceConfig.setMethods(methods);
		
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("dubbo");
		
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress("zookeeper://nbmszk1:2117?backup=nbmszk2:2118,nbmszk3:2119");
		
		referenceConfig.setRegistry(registryConfig);
		//com.alibaba.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance
		UserService userService = referenceConfig.get();
		System.out.println(userService);
		ProductBean productBean1 = new ProductBean();
		productBean1.setId(10);
		ProductBean productBean2 = new ProductBean();
		productBean2.setId(1);
		
		for(int i=0;i<10;i++){
			userService.updateProductStock(productBean1);
			userService.updateProductStock(productBean2);
		}
	}
	
}
