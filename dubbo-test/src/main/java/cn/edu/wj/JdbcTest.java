package cn.edu.wj;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
@ContextConfiguration(locations = { "classpath:config/spring-jdbc.xml" })
public class JdbcTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    final CountDownLatch startLatch = new CountDownLatch(1);
	final CountDownLatch endLatch = new CountDownLatch(StockConst.Thread_NUM);
    
	@Before
    public void before(){
		this.jdbcTemplate.update("update t_order set money="+StockConst.Product_NUM+" where order_id=1");
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
    	
    	System.out.println("jdbc秒杀总共耗时【"+(System.currentTimeMillis()-startTime)+"】毫秒");
    }
    
    public int skillAmt(int money){
    	return this.jdbcTemplate.update("update t_order set money= money-"+money+" where order_id=1 and money-"+money+" >=0");
    }
    
    @Test
    public void test(){
    	int result = this.jdbcTemplate.update("update t_order set money= money-"+2000+" where order_id=1 and money-"+2000+" >=0");
    	System.out.println(result);
    }
    
    
}
