package cn.edu.wj.factory;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;


/**
 * 分布式锁简单模板
 * 
 * @author jwu
 */
public class SimpleLockTemplate implements DistributedLockTemplate {

	private static final long DEFAULT_TIMEOUT = 5;
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
	private RedissonClient redisson;

	public SimpleLockTemplate() {
	}

	public SimpleLockTemplate(RedissonClient redisson) {
		this.redisson = redisson;
	}

	@Override
	public <T> T lock(DistributedLockCallback<T> callback) {
		return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
	}

	@Override
	public <T> T lock(DistributedLockCallback<T> callback, long leaseTime,
			TimeUnit timeUnit) {
		RLock lock = null;
		try {
			lock = redisson.getLock(callback.getLockName());
			lock.lock(leaseTime, timeUnit);
			return callback.process();
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}
}
