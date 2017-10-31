package cn.edu.wj.rpc.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeepreDiscovery {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeepreDiscovery.class);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;
    
    private final ZkClient zkClient;

    public ZookeepreDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
        
        zkClient = new ZkClient(this.registryAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_SESSION_TIMEOUT);

        watchNode(zkClient);
    }

    public String discover() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                LOGGER.debug("using only data: {}", data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("using random data: {}", data);
            }
        }
        return data;
    }

    private void watchNode(final ZkClient zkClient) {
        	List<String> nodeList = zkClient.getChildren(Constant.ZK_REGISTRY_PATH);
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
            	String data = zkClient.readData(Constant.ZK_REGISTRY_PATH + "/" + node);
                dataList.add(data);
            }
            LOGGER.debug("node data: {}", dataList);
            this.dataList = dataList;
    }
}
