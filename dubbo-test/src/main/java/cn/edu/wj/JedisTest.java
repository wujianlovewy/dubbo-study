package cn.edu.wj;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.util.StockConst;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
* @author jwu
* @date 2017-2-28 上午11:12:03
* @Description 
**/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-jedis.xml" })
public class JedisTest {

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
    public void mainTest(){
    	long startTime = System.currentTimeMillis();
    	for(int i=0;i<StockConst.Thread_NUM;i++){
	    	new Thread(){
	    		@Override
	    		public void run(){
	    			try {
						startLatch.await();
						int result = skillAmt(5);
		    			while(result==-1){
		    				LockSupport.parkNanos(1);
		    				result = skillAmt(5);
		    			}
		    			System.out.println("支付结果: "+Thread.currentThread().getName()+" - "+result);
		    			if(result==0){
		    				System.out.println(Thread.currentThread().getName()+" 抢购失败!");
		    			}else if(result>0){
		    				System.out.println(Thread.currentThread().getName()+" 抢购成功!");
		    			}
		    			endLatch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
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
    	
    	System.out.println("redis秒杀总共耗时【"+(System.currentTimeMillis()-startTime)+"】毫秒");
    }
    
    public int skillAmt(int money){
    	Jedis jedis = null;
    	try{
    		jedis = jedisPool.getResource();
    		String watch = jedis.watch("balance"); 
    		if(null==watch || "".equals(watch)){
    			return -1;
    		}
    		int balance = Integer.parseInt(jedis.get("balance"));
	        System.out.println(Thread.currentThread().getName()+" --  money:"+money);  
	        Transaction multi = jedis.multi(); 
	        if(balance<money){
	        	return 0;
	        }else{
	        	multi.decrBy("balance", money);
	        }
	        List<Object> exec = multi.exec();
	        System.out.println(Thread.currentThread().getName()+"---"+exec);
	        if(null==exec || exec.size()==0){
	        	return -1;
	        }
	        return exec.size();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(null!=jedis){
    			jedis.unwatch();  
    			jedis.close();
    			jedis = null;
    		}
    	}
		return -1; 
    }
    
    @Test
    public void test(){
    	Jedis jedis = null;
    	try{
    		jedis = jedisPool.getResource();
    		String watch = jedis.watch("hello");  
	        System.out.println(Thread.currentThread().getName()+"--"+watch);  
	        Transaction multi = jedis.multi();  
	        multi.set("hello", "23432");  
	        System.out.println("休眠...");
	        Thread.sleep(5000);
	        List<Object> exec = multi.exec();
	        System.out.println("---"+exec);  
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(null!=jedis){
    			jedis.unwatch();  
    			jedis.close();
    			jedis = null;
    		}
    	}
    }
    
}
