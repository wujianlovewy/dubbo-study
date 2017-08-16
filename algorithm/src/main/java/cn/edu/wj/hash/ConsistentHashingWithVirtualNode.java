package cn.edu.wj.hash;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * 带虚拟节点的一致性Hash算法
 * 参考资料: http://www.cnblogs.com/xrq730/p/5186728.html
 */
public class ConsistentHashingWithVirtualNode {

	/**
	 * 待添加入Hash环的服务器列表
	 */
	private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

	/**
	 * 真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
	 */
	private static List<String> realNodes = new LinkedList<String>();

	/**
	 * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
	 */
	private static SortedMap<Integer, String> virtualNodesMap = new TreeMap<Integer, String>();

	/**
	 * 虚拟节点的数目，为了演示需要，一个真实结点对应5个虚拟节点
	 */
	private static final int VIRTUAL_NODES = 10;

	private static final String NODE_FIX = "&&VN";

	static {
		// 先把原始服务器地址添加到真实结点列表中
		for (String server : servers) {
			realNodes.add(server);
		}

		// 再添加虚拟节点，遍历LinkedList使用foreach循环效率会比较高
		for (String node : realNodes) {
			for (int i = 0; i < VIRTUAL_NODES; i++) {
				String virtualNode = node + NODE_FIX + i;
				int hash = getHash(virtualNode);
				System.out.println("虚拟节点[" + virtualNode + "]被添加, hash值为"
						+ hash);
				virtualNodesMap.put(hash, virtualNode);
			}
		}
	}

	/**
	 * hash路由到具体服务
	 */
	public static String getRouterServer(String node) {
		// 得到带路由的结点的Hash值
		int fromKey = getHash(node);
		// 得到大于当前节点hash值的server的Map
		SortedMap<Integer, String> subMap = virtualNodesMap.tailMap(fromKey);
		String virtualNode = subMap.get(subMap.firstKey());
		return virtualNode.substring(0, virtualNode.indexOf(NODE_FIX));
	}

	public static void main(String[] args) {
		String[] nodes = { "127.0.0.1:1111", "221.226.0.1:2222",
				"10.211.0.1:3333" };
		for (int i = 0; i < nodes.length; i++)
			System.out.println("[" + nodes[i] + "]的hash值为" + getHash(nodes[i])
					+ ", 被路由到结点[" + getRouterServer(nodes[i]) + "]");
	}

}
