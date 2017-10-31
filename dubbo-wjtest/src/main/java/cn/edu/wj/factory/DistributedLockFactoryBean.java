package cn.edu.wj.factory;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.FactoryBean;

/**
 * 创建分布式锁模板实例的工厂Bean
 */
public class DistributedLockFactoryBean implements
		FactoryBean<DistributedLockTemplate> {
	private Logger logger = Logger.getLogger(DistributedLockFactoryBean.class);

	private LockInstanceMode mode;

	private DistributedLockTemplate distributedLockTemplate;

	private RedissonClient redisson;

	@PostConstruct
	public void init() {
		logger.debug("初始化分布式锁模板");
		InputStream inputStream = null;
		Config config = null;
		try {
			inputStream = DistributedLockFactoryBean.class.getClassLoader()
					.getResourceAsStream("redisson.properties");
			config = Config.fromJSON(inputStream);
		} catch (IOException e) {
			logger.error("读取Redisson配置失败", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		redisson = Redisson.create(config);
	}

	@PreDestroy
	public void destroy() {
		logger.debug("销毁分布式锁模板");
		redisson.shutdown();
	}

	@Override
	public DistributedLockTemplate getObject() throws Exception {
		switch (mode) {
			case SINGLE:
				distributedLockTemplate = new SimpleLockTemplate(redisson);
				break;
		}
		return distributedLockTemplate;
	}

	@Override
	public Class<?> getObjectType() {
		return DistributedLockTemplate.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setMode(String mode) {
		if (null != mode && "".equals(mode)) {
			throw new IllegalArgumentException("未找到dlm.redisson.mode配置项");
		}
		this.mode = LockInstanceMode.parse(mode);
		if (this.mode == null) {
			throw new IllegalArgumentException("不支持的分布式锁模式");
		}
	}

	private enum LockInstanceMode {
		SINGLE;
		public static LockInstanceMode parse(String name) {
			for (LockInstanceMode modeIns : LockInstanceMode.values()) {
				if (modeIns.name().equals(name.toUpperCase())) {
					return modeIns;
				}
			}
			return null;
		}
	}
}
