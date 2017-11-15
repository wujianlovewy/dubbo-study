package cn.edu.wj.sign;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 两种方式的区别，第一种就是使用你给定的key作为密钥，当与其他客户端进行通信，加解密不是由同一方操作的时候推荐使用，
 * 第二种是根据你给的key来生成一个新的key，这个新的key才是真正加密时使用的key，所以如果用第一种加密出来的密文用第二种来解密，是解不出来的。
　　另外，java中只提供了3倍长3des的算法，也就是key的长度必须是24字节，如果想要使用2倍长3des，
        需要自己将后8个字节补全（就是将16个字节的前8个字节补到最后，变成24字节）。
       如果提供的key不足24字节，将会报错，如果超过24字节，将会截取前24字节作为key）
 * @author jwu
 * 参考: https://www.cnblogs.com/wsss/p/6925090.html
 * jvm内存:http://www.cnblogs.com/lewis0077/p/5143268.html
 */
public class DESedeCoder2 {

	/**
	 * 密钥算法
	 * */
	public static final String KEY_ALGORITHM="DESede";
	
	/**
	 * 加密/解密算法/工作模式/填充方式
	 * NoPadding---无填充
	 * */
	
	public static final String CIPHER_ALGORITHM="DESede/ECB/NoPadding";
	
	/**
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initkey(String key) throws Exception{
		
		//实例化密钥生成器
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		byte[] keyByte = HexString2Bytes(key);
		//初始化密钥生成器
		kg.init(new SecureRandom(keyByte));
		//生成密钥 
		SecretKey secretKey=kg.generateKey();
		//获取二进制密钥编码形式
		return secretKey.getEncoded();
	}
	
	public static Key getKey(String key) throws Exception{
		//实例化密钥生成器
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		byte[] keyByte = HexString2Bytes(key);
		//初始化密钥生成器
		kg.init(new SecureRandom(keyByte));
		//生成密钥 
		SecretKey secretKey=kg.generateKey();
		return secretKey;
	}
	
	public static byte[] HexString2Bytes(String key){
		try {
			byte[] keyByteAry = Hex.decodeHex(key.toCharArray());
			byte[] newkeyAry = new byte[24];
			if (keyByteAry.length == 16) {
				System.arraycopy(keyByteAry, 0, newkeyAry, 0, 16);
				System.arraycopy(keyByteAry, 0, newkeyAry, 16, 8);
			} else if (keyByteAry.length == 8) {
				System.arraycopy(keyByteAry, 0, newkeyAry, 0, 8);
				System.arraycopy(keyByteAry, 0, newkeyAry, 8, 8);
				System.arraycopy(keyByteAry, 0, newkeyAry, 16, 8);
			} else {
				newkeyAry = keyByteAry;
			}
			return newkeyAry;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String BytetoHexString(byte[] data){
		try {
			return new String(Hex.encodeHex(data));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return Key 密钥
	 * */
	public static Key toKey(byte[] key) throws Exception{
		byte[] newkeyAry = new byte[24];
		if (key.length == 16) {
			System.arraycopy(key, 0, newkeyAry, 0, 16);
			System.arraycopy(key, 0, newkeyAry, 16, 8);
		} else if (key.length == 8) {
			System.arraycopy(key, 0, newkeyAry, 0, 8);
			System.arraycopy(key, 0, newkeyAry, 8, 8);
			System.arraycopy(key, 0, newkeyAry, 16, 8);
		} else {
			newkeyAry = key;
		}
		  //实例化Des密钥  
        DESedeKeySpec dks=new DESedeKeySpec(newkeyAry);  
        //实例化密钥工厂  
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //生成密钥  
        SecretKey secretKey=keyFactory.generateSecret(dks);  
        return secretKey;  
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		//还原密钥
		Key k=toKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	/**
	 * 解密数据
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据 
	 * */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		//欢迎密钥
		Key k =toKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data,String key) throws Exception{
		//还原密钥
		Key k=getKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	/**
	 * 解密数据
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data,String key) throws Exception{
		//欢迎密钥
		Key k =getKey(key);
		//实例化
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/*byte[] bytes = new byte[32 / 2];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		
		System.out.println("生成秘钥:"+byte2HexStr(bytes));
		
		//初始化密钥
		String oldKey = BytetoHexString(bytes);*/
		String oldKey = "8B2713C461577630A0BD495026442D4D";
		
		String str="测试3des";
		System.out.println("原文："+str);
		
		String keyByte = byte2HexStr(str.getBytes());
		//oldKey="6496C14C928B9B9D681021DDD6C4A38D";
		byte[] key = DESedeCoder2.initkey(oldKey);
		System.out.println("oldKey:"+oldKey+", 秘钥长度:"+oldKey.length());
		//加密数据
		byte[] data=DESedeCoder2.encrypt(hexStr2Bytes(keyByte), key);
		System.out.println("加密后："+Base64.encodeBase64String(data));
		//解密数据
		data=DESedeCoder2.decrypt(data, key);
		System.out.println("解密后："+new String(data));
	}
	
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}
	
	private static byte uniteBytes(String src0, String src1) {
		if (src0.equals("=")) {
			src0 = "D";
		}

		if (src1.equals("=")) {
			src1 = "D";
		}

		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}
	
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	public static void test1() throws Exception{
		String str="DESede2";
		System.out.println("原文："+str);
		//初始化密钥
		byte[] keyData = {1,2,3,4,5,6,7,8};
		byte[] keyData2 = {1,2,3,4,5,6,7,8,0,9,11,12,13,14,15,16};
		String oldKey = BytetoHexString(keyData2);
		//oldKey="6496C14C928B9B9D681021DDD6C4A38D";
		System.out.println("oldKey:"+oldKey+", 秘钥长度:"+oldKey.length());
		//加密数据
		byte[] data=DESedeCoder2.encrypt(str.getBytes(), oldKey);
		System.out.println("加密后："+Base64.encodeBase64String(data));
		//解密数据
		data=DESedeCoder2.decrypt(data, oldKey);
		System.out.println("解密后："+new String(data));
	}
	
}
