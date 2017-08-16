package cn.edu.wj.hash;

import java.util.SortedMap;
import java.util.TreeMap;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * 不带虚拟节点的一致性Hash算法
 * 
 * @author jwu
 */
public class ConsistentHashingWithoutVirtualNode {

	/**
	 * 待添加入Hash环的服务器列表
	 */
	private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

	/**
	 * key表示服务器的hash值，value表示服务器的名称
	 */
	private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

	static {
		for (String server : servers) {
			int hash = getHash(server);
			System.out.println("[" + server + "]加入sortedMap中,其hash值为:" + hash);
			sortedMap.put(hash, server);
		}
	}

	/**
	 * hash路由到具体服务
	 */
	public static String getRouterServer(String node) {
		// 得到带路由的结点的Hash值
		int fromKey = getHash(node);
		// 得到大于当前节点hash值的server的Map
		SortedMap<Integer, String> subMap = sortedMap.tailMap(fromKey);
		// 返回当前node顺时针最近的server
		return subMap.get(subMap.firstKey());
	}

	public static void main(String[] args) {
		String[] nodes = { "127.0.0.1:1111", "221.226.0.1:2222",
				"10.211.0.1:3333" };
		for (int i = 0; i < nodes.length; i++)
			System.out.println("[" + nodes[i] + "]的hash值为" + getHash(nodes[i])
					+ ", 被路由到结点[" + getRouterServer(nodes[i]) + "]");
	}

}
