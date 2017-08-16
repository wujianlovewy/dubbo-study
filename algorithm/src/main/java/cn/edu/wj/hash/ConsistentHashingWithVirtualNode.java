package cn.edu.wj.hash;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * ������ڵ��һ����Hash�㷨
 * �ο�����: http://www.cnblogs.com/xrq730/p/5186728.html
 */
public class ConsistentHashingWithVirtualNode {

	/**
	 * �������Hash���ķ������б�
	 */
	private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

	/**
	 * ��ʵ����б�,���ǵ����������ߡ����ߵĳ���������ӡ�ɾ���ĳ�����Ƚ�Ƶ��������ʹ��LinkedList�����
	 */
	private static List<String> realNodes = new LinkedList<String>();

	/**
	 * ����ڵ㣬key��ʾ����ڵ��hashֵ��value��ʾ����ڵ������
	 */
	private static SortedMap<Integer, String> virtualNodesMap = new TreeMap<Integer, String>();

	/**
	 * ����ڵ����Ŀ��Ϊ����ʾ��Ҫ��һ����ʵ����Ӧ5������ڵ�
	 */
	private static final int VIRTUAL_NODES = 10;

	private static final String NODE_FIX = "&&VN";

	static {
		// �Ȱ�ԭʼ��������ַ��ӵ���ʵ����б���
		for (String server : servers) {
			realNodes.add(server);
		}

		// ���������ڵ㣬����LinkedListʹ��foreachѭ��Ч�ʻ�Ƚϸ�
		for (String node : realNodes) {
			for (int i = 0; i < VIRTUAL_NODES; i++) {
				String virtualNode = node + NODE_FIX + i;
				int hash = getHash(virtualNode);
				System.out.println("����ڵ�[" + virtualNode + "]�����, hashֵΪ"
						+ hash);
				virtualNodesMap.put(hash, virtualNode);
			}
		}
	}

	/**
	 * hash·�ɵ��������
	 */
	public static String getRouterServer(String node) {
		// �õ���·�ɵĽ���Hashֵ
		int fromKey = getHash(node);
		// �õ����ڵ�ǰ�ڵ�hashֵ��server��Map
		SortedMap<Integer, String> subMap = virtualNodesMap.tailMap(fromKey);
		String virtualNode = subMap.get(subMap.firstKey());
		return virtualNode.substring(0, virtualNode.indexOf(NODE_FIX));
	}

	public static void main(String[] args) {
		String[] nodes = { "127.0.0.1:1111", "221.226.0.1:2222",
				"10.211.0.1:3333" };
		for (int i = 0; i < nodes.length; i++)
			System.out.println("[" + nodes[i] + "]��hashֵΪ" + getHash(nodes[i])
					+ ", ��·�ɵ����[" + getRouterServer(nodes[i]) + "]");
	}

}
