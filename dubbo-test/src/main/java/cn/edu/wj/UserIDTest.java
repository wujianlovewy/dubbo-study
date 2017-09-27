package cn.edu.wj;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.FastDateFormat;

import cn.itcast.dubbo.service.UserService;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * http://www.bijishequ.com/subject/315 程序员必须掌握的8大排序算法
 * http://blog.csdn.net/chenssy/article/details/70657913  死磕Java并发
 * @author jwu
 *
 */
public class UserIDTest {
	private static AtomicLong atomic = new AtomicLong();
	static String PARTTERN_STR = "yyyyMMddhhmmssSSS";
	
	//SimpleDateFormat线程不安全并且效率没有FastDateFormat高
	static FastDateFormat format = FastDateFormat.getInstance(PARTTERN_STR);
	static DecimalFormat df = new DecimalFormat("0000");
	
	public static String generateId(){
		String serialNumber = format.format(new Date());
		long num = atomic.getAndIncrement();
		return serialNumber+num;
	}
	
	public static void main(String[] args) throws Exception{
		testForUserID();
	}
	
	public static void testForUserID(){
		final UserService userService = getRemoteService(UserService.class);
		int count = 1;
		long startTime = System.currentTimeMillis();
		for(int i=0;i<count;i++){
			userService.generateId();
		}
		System.out.println("generateId: "+(System.currentTimeMillis()-startTime));
	}
	
	public static void testFor(){
		int count = 2000000;
		long startTime = System.currentTimeMillis();
		for(int i=0;i<count;i++){
			UserIDTest.generateId();
		}
		System.out.println("generateId: "+(System.currentTimeMillis()-startTime));
	}
	
	public static <T> T getRemoteService(Class<T> clazz){
		ApplicationConfig config = new ApplicationConfig();
		config.setName("dubbo");
		
		ReferenceConfig<T> referenceConfig = new ReferenceConfig<T>();
		referenceConfig.setInterface(clazz);
		referenceConfig.setApplication(config);
		referenceConfig.setTimeout(60000);
		
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("dubbo");
		
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress("zookeeper://nbmszk1:2117?backup=nbmszk2:2118,nbmszk3:2119");
		
		referenceConfig.setRegistry(registryConfig);
		return referenceConfig.get();
	}
	
	public static void testMutiThread() throws Exception{
		final UserService userService = getRemoteService(UserService.class);
		int count = 10000;
		final CountDownLatch end = new CountDownLatch(count);
		final CountDownLatch start = new CountDownLatch(1);
		long startTime = System.currentTimeMillis();
		for(int i=0;i<count;i++){
			new Thread(){
				@Override
				public void run(){
					try {
						//start.await();
						//UserIDTest.generateId();
						//System.out.println(userService.generateId());
						userService.generateId();
						end.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		//start.countDown();
		end.await();
		System.out.println(System.currentTimeMillis()-startTime);
	}
}
