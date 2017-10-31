package cn.edu.wj.rpc.demo;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;

/**
 * 实现zk注册中心
 * @author jwu
 *
 */
public class ZookeeperRegister {

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(ZookeeperRegister.class);

	private String registryAddress;
	
	private final ZkClient zkClient;

	ZookeeperRegister(String registryAddress){
		this.registryAddress = registryAddress;
		zkClient = new ZkClient(this.registryAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_SESSION_TIMEOUT);
	}
	
	public void register(String data){
		if(null!=data){
			if(null!=zkClient){
				this.createNode(zkClient, data);
			}
		}
	}
	
	private void createNode(ZkClient zkClient, String data) {
		String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            logger.debug("create registry node: {}", registryPath);
        }
      /*  // 创建 service 节点（持久）
        String servicePath = Constant.ZK_DATA_PATH;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            logger.debug("create service node: {}", servicePath);
        }*/
        
        String path = zkClient.createEphemeralSequential(Constant.ZK_DATA_PATH, data);
        logger.debug("create zookeeper node ({} => {})", path, data);
    }
	
	public ZkClient getZkClient() {
		return zkClient;
	}

	public static void main(String[] args) {
		ZookeeperRegister zr = new ZookeeperRegister("nbmszk1:2117");
		zr.getZkClient().delete(Constant.ZK_REGISTRY_PATH+"/data");
	}
	
}
