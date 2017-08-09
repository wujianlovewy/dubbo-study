package cn.edu.wj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-redisson.xml" })
public class RedissonTest {

	@Autowired
	private RedissonClient redissonClient;
	
	@Test
	public void test(){
		long cnt = redissonClient.getKeys().count();
		System.out.println("key cnt: "+cnt);
	}
	
	public static void main(String[] args) throws Exception {

		Config config = new Config();
		config.useSingleServer().setAddress("redis://10.148.181.134:6390");
        RedissonClient redisson =  Redisson.create(config);
        RLock lock = redisson.getLock("haogrgr");
        lock.lock();
        try {
            System.out.println("hagogrgr");
        }
        finally {
            lock.unlock();
        }

        redisson.shutdown();

    }

	
}
