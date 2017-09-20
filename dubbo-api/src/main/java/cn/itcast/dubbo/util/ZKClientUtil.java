package cn.itcast.dubbo.util;

import java.io.Serializable;
import java.util.Date;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

@Component
public class ZKClientUtil implements Serializable{
	private static final long serialVersionUID = -8740681768727757511L;
	private static final String ZK_CONNECT_HOST = "nbmszk1:2117,nbmszk2:2118,nbmszk3:2119";
	private static final int SESSION_TIMEOUT = 5000;
	
	private ZkClient zkClient = null;
	
	private ZKClientUtil(){
		zkClient = new ZkClient(ZK_CONNECT_HOST, SESSION_TIMEOUT);
	}
	
	public ZkClient connect(){
			return zkClient;
	}
	
	
}
