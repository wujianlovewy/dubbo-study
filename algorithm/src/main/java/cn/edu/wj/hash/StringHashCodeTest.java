package cn.edu.wj.hash;

import static cn.edu.wj.hash.HashUtil.getHash;

/**
 * String的hashCode()方法运算结果
 * @author jwu
 *
 */
public class StringHashCodeTest {

	static final int MAX_SEGMENTS = 1 << 16;
	
	public static void main(String[] args) {
		System.out.println(test2("heqlloa!".hashCode()));
	}
	
	public static int test2(int hash){
		int concurrencyLevel = 1 << 16;
		int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;
        System.out.println("concurrencyLevel:"+concurrencyLevel+",ssize:"+ssize+", sshift:"+sshift);
        
        return (hash>>>segmentShift)&segmentMask;
	}
	
	public static void test1(){
		System.out.println("192.168.0.0:111的哈希值：" + "192.168.0.0:1111".hashCode());
        System.out.println("192.168.0.1:111的哈希值：" + "192.168.0.1:1111".hashCode());
        System.out.println("192.168.0.2:111的哈希值：" + "192.168.0.2:1111".hashCode());
        System.out.println("192.168.0.3:111的哈希值：" + "192.168.0.3:1111".hashCode());
        System.out.println("192.168.0.4:111的哈希值：" + "192.168.0.4:1111".hashCode());
        System.out.println("192.168.1.0:111的哈希值：" + "192.168.1.0:1111".hashCode());
        System.out.println("192.168.1.0:111的FNV1_32_HASH：" + getHash("192.168.1.0:1111"));
	}
	
}
