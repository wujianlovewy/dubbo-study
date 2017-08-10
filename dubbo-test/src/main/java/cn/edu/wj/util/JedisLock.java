package cn.edu.wj.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisLock{
	
	private final static long ACCQUIRE_LOCK_TIMEOUT = 1;
    private final static int EXPIRE_SECOND = 5;//锁失效时间
    private final static long WAIT_TIME = 1;
    
    private final JedisPool jedisPool;
    private final long acquireTimeout;
    private final int expireSecond;
    private final long maxWaitTime;
    
    public JedisLock(final JedisPool jedisPool,final long acquireTimeout, 
    		final int expireSecond,
            final long maxWaitTime) {
    	this.jedisPool = jedisPool;
        this.acquireTimeout = acquireTimeout;
        this.expireSecond = expireSecond;
        this.maxWaitTime = maxWaitTime;
    }
    
    public JedisLock(final JedisPool jedisPool){
    	this(jedisPool, ACCQUIRE_LOCK_TIMEOUT, EXPIRE_SECOND, WAIT_TIME);
    }
    
    public boolean lock(final String key){
    	long timeOut = System.currentTimeMillis() + this.acquireTimeout * 1000;
    	Jedis jedis = null;
    	boolean lockRet = false;
    	try{
	    	while(true){
	    		String expireTimeActual = String.valueOf(System.currentTimeMillis() + this.expireSecond*1000);
	    		jedis = this.jedisPool.getResource();
	    		long ret = jedis.setnx(key, expireTimeActual);
	    		if(ret==1){
	    			lockRet = true;
	    			break;
	    		}else{
	    			String redisExpireTime = jedis.get(key);
	    			//redis中的锁已经失效
	    			if(null!=redisExpireTime 
	    					&& Long.parseLong(redisExpireTime)<System.currentTimeMillis()){
	    				String oldExpireTime = jedis.getSet(key, expireTimeActual);
	    				if(null!=oldExpireTime && oldExpireTime.equals(redisExpireTime)){
	    					lockRet = true;
	    					break;
	    				}
	    			}
	    		}

	    		if(null!=jedis){
	    			this.jedisPool.returnResource(jedis);
	    			jedis = null;
	    		}
	    		if(this.acquireTimeout<=0 || timeOut < System.currentTimeMillis()){
	    			lockRet = false;
	    			break;
	    		}
	    		
	    		//LockSupport.parkNanos(this.maxWaitTime);
	    		/*try {
					Thread.sleep(maxWaitTime);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
	    	}
	    	return lockRet;
    	}catch (Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		if(null!=jedis){
    			this.jedisPool.returnResource(jedis);
    			jedis = null;
    		}
    	}
    }
    
    public long unlock(final String key){
    	Jedis jedis = this.jedisPool.getResource();
    	long delRet = jedis.del(key);
    	if(jedis!=null){
    		this.jedisPool.returnResource(jedis);
    		jedis = null;
    	}
    	return delRet;
    }
    
}
