package cn.edu.wj.hash;

public  final class HashUtil {
	
	/**
	 * ʹ��FNV1_32_HASH�㷨�����������Hashֵ,���ﲻʹ����дhashCode�ķ���������Ч��û���� 
	 * @param key
	 * @return
	 */
	public static int getHash(String key){
		final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        
        // ����������ֵΪ������ȡ�����ֵ
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
	}
}
