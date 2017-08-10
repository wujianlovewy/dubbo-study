package cn.edu.wj;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.edu.wj.util.JedisLock;
import cn.edu.wj.util.StockConst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-jedis.xml" })
public class JedisLockTest{
	
	@Autowired
    private JedisPool jedisPool;
	
	final CountDownLatch startLatch = new CountDownLatch(1);
	final CountDownLatch endLatch = new CountDownLatch(StockConst.Thread_NUM);
	
	@Before
    public void before(){
		Jedis jedis = null;
    	try{
    		jedis = jedisPool.getResource();
    		jedis.set("balance",StockConst.Product_NUM+"");
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(null!=jedis){
    			jedis.close();
    			jedis = null;
    		}
    	}
    }
	
	@Test
	public void lockTest(){
		long startTime = System.currentTimeMillis();
		final JedisLock lock = new JedisLock(jedisPool);
    	for(int i=0;i<StockConst.Thread_NUM;i++){
	    	new Thread(){
	    		@Override
	    		public void run(){
	    			boolean lockRet = false;
	    			try {
						startLatch.await();
						lockRet = lock.lock("lock");
						if(lockRet){
							/*int result = skillAmt(5);
			    			System.out.println("支付结果: "+Thread.currentThread().getName()+" - "+result);
			    			if(result==0){
			    				System.out.println(Thread.currentThread().getName()+" 抢购失败!");
			    			}else if(result>0){
			    				System.out.println(Thread.currentThread().getName()+" 抢购成功!");
			    			}*/
							System.out.println(Thread.currentThread().getName()+" 获取锁成功!");
						}else{
							System.out.println(Thread.currentThread().getName()+" 获取失败!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(lockRet){
							System.out.println(Thread.currentThread().getName()+" 开始释放锁!");
							long unlock = lock.unlock("lock");
							System.out.println(Thread.currentThread().getName()+" 释放锁!"+unlock);
						}
						endLatch.countDown();
					}
	    		}
	    	}.start();
    	}
    	startLatch.countDown();
    	try {
    		endLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("redis+lock秒杀总共耗时【"+(System.currentTimeMillis()-startTime)+"】毫秒");
	}
	
	public int skillAmt(int money){
    	Jedis jedis = null;
    	try{
    		jedis = jedisPool.getResource();
    		int balance = Integer.parseInt(jedis.get("balance"));
	        System.out.println(Thread.currentThread().getName()+" --  money:"+money);  
	        if(balance<money){
	        	return 0;
	        }else{
	        	if(jedis.decrBy("balance", money)>0){
	        		return 1;
	        	}
	        }
	        return -1;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return -1; 
    }
	
	@Test
	public void test(){
		JedisLock lock = new JedisLock(jedisPool);
		boolean lockRet = false;
		try{
			lockRet = lock.lock("lock");
			if(lockRet){
				System.out.println("获取锁成功!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(lockRet){
				lock.unlock("lock");
			}
		}
	}
    
}
