package cn.edu.wj;

import java.util.concurrent.locks.LockSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-jedis.xml" })
public class JedisLockTest {

	@Autowired
    private JedisPool jedisPool;
	
	@Test
	public void test(){
		Jedis jedis = this.jedisPool.getResource();
		JedisLock lock = new JedisLock(jedis);
		boolean result = false;
		try{
			result = lock.lock("lock");
			if(result){
				System.out.println("获取锁成功!");
			}
		}catch(Exception e){
			
		}finally{
			if(result){
				lock.unlock("lock");
			}
		}
	}
}

class JedisLock{
	private final static long ACCQUIRE_LOCK_TIMEOUT = 10;
    private final static int EXPIRE_SECOND = 5;//锁失效时间
    private final static long WAIT_TIME = 100;
    
    private final Jedis jedis;
    private final long acquireTimeout;
    private final int expireSecond;
    private final long maxWaitTime;
    
    public JedisLock(final Jedis jedis,final long acquireTimeout, 
    		final int expireSecond,
            final long maxWaitTime) {
    	this.jedis = jedis;
        this.acquireTimeout = acquireTimeout;
        this.expireSecond = expireSecond;
        this.maxWaitTime = maxWaitTime;
    }
    
    public JedisLock(final Jedis jedis){
    	this(jedis, ACCQUIRE_LOCK_TIMEOUT, EXPIRE_SECOND, WAIT_TIME);
    }
    
    public boolean lock(final String key){
    	long timeOut = System.currentTimeMillis() + this.acquireTimeout * 1000;
    	while(true){
    		String expireTimeActual = String.valueOf(System.currentTimeMillis() + this.expireSecond*1000);
    		long ret = this.jedis.setnx(key, expireTimeActual);
    		if(ret==1){
    			return true;
    		}else{
    			String redisExpireTime = this.jedis.get(key);
    			//redis中的锁已经失效
    			if(null!=redisExpireTime && Long.parseLong(redisExpireTime)<System.currentTimeMillis()){
    				redisExpireTime = this.jedis.getSet(key, expireTimeActual);
    				if(Long.parseLong(redisExpireTime)<System.currentTimeMillis()){
    					return true;
    				}
    			}
    		}
    		
    		if(this.acquireTimeout<=0 || timeOut < System.currentTimeMillis()){
    			return false;
    		}
    		
    		LockSupport.parkNanos(this.maxWaitTime);
    	}
    }
    
    public void unlock(final String key){
    	this.jedis.del(key);
    }
    
    
    
    
    
}
