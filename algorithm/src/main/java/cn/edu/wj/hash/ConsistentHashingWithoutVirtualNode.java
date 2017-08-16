package cn.edu.wj.hash;

import java.util.SortedMap;
import java.util.TreeMap;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * ��������ڵ��һ����Hash�㷨
 * 
 * @author jwu
 */
public class ConsistentHashingWithoutVirtualNode {

	/**
	 * �������Hash���ķ������б�
	 */
	private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

	/**
	 * key��ʾ��������hashֵ��value��ʾ������������
	 */
	private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

	static {
		for (String server : servers) {
			int hash = getHash(server);
			System.out.println("[" + server + "]����sortedMap��,��hashֵΪ:" + hash);
			sortedMap.put(hash, server);
		}
	}

	/**
	 * hash·�ɵ��������
	 */
	public static String getRouterServer(String node) {
		// �õ���·�ɵĽ���Hashֵ
		int fromKey = getHash(node);
		// �õ����ڵ�ǰ�ڵ�hashֵ��server��Map
		SortedMap<Integer, String> subMap = sortedMap.tailMap(fromKey);
		// ���ص�ǰnode˳ʱ�������server
		return subMap.get(subMap.firstKey());
	}

	public static void main(String[] args) {
		String[] nodes = { "127.0.0.1:1111", "221.226.0.1:2222",
				"10.211.0.1:3333" };
		for (int i = 0; i < nodes.length; i++)
			System.out.println("[" + nodes[i] + "]��hashֵΪ" + getHash(nodes[i])
					+ ", ��·�ɵ����[" + getRouterServer(nodes[i]) + "]");
	}

}
