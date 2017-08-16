package cn.edu.wj.hash;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * String的hashCode()方法运算结果
 * @author jwu
 *
 */
public class StringHashCodeTest {

	public static void main(String[] args) {
		System.out.println("192.168.0.0:111的哈希值：" + "192.168.0.0:1111".hashCode());
        System.out.println("192.168.0.1:111的哈希值：" + "192.168.0.1:1111".hashCode());
        System.out.println("192.168.0.2:111的哈希值：" + "192.168.0.2:1111".hashCode());
        System.out.println("192.168.0.3:111的哈希值：" + "192.168.0.3:1111".hashCode());
        System.out.println("192.168.0.4:111的哈希值：" + "192.168.0.4:1111".hashCode());
        System.out.println("192.168.1.0:111的哈希值：" + "192.168.1.0:1111".hashCode());
        System.out.println("192.168.1.0:111的FNV1_32_HASH：" + getHash("192.168.1.0:1111"));
	}
	
}
