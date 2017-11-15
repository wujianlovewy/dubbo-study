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
 * ���ַ�ʽ�����𣬵�һ�־���ʹ���������key��Ϊ��Կ�����������ͻ��˽���ͨ�ţ��ӽ��ܲ�����ͬһ��������ʱ���Ƽ�ʹ�ã�
 * �ڶ����Ǹ��������key������һ���µ�key������µ�key������������ʱʹ�õ�key����������õ�һ�ּ��ܳ����������õڶ��������ܣ��ǽⲻ�����ġ�
�������⣬java��ֻ�ṩ��3����3des���㷨��Ҳ����key�ĳ��ȱ�����24�ֽڣ������Ҫʹ��2����3des��
        ��Ҫ�Լ�����8���ֽڲ�ȫ�����ǽ�16���ֽڵ�ǰ8���ֽڲ�����󣬱��24�ֽڣ���
       ����ṩ��key����24�ֽڣ����ᱨ���������24�ֽڣ������ȡǰ24�ֽ���Ϊkey��
 * @author jwu
 * �ο�: https://www.cnblogs.com/wsss/p/6925090.html
 * jvm�ڴ�:http://www.cnblogs.com/lewis0077/p/5143268.html
 */
public class DESedeCoder2 {

	/**
	 * ��Կ�㷨
	 * */
	public static final String KEY_ALGORITHM="DESede";
	
	/**
	 * ����/�����㷨/����ģʽ/��䷽ʽ
	 * NoPadding---�����
	 * */
	
	public static final String CIPHER_ALGORITHM="DESede/ECB/NoPadding";
	
	/**
	 * @return byte[] ��������Կ
	 * */
	public static byte[] initkey(String key) throws Exception{
		
		//ʵ������Կ������
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		byte[] keyByte = HexString2Bytes(key);
		//��ʼ����Կ������
		kg.init(new SecureRandom(keyByte));
		//������Կ 
		SecretKey secretKey=kg.generateKey();
		//��ȡ��������Կ������ʽ
		return secretKey.getEncoded();
	}
	
	public static Key getKey(String key) throws Exception{
		//ʵ������Կ������
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		byte[] keyByte = HexString2Bytes(key);
		//��ʼ����Կ������
		kg.init(new SecureRandom(keyByte));
		//������Կ 
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
	 * ת����Կ
	 * @param key ��������Կ
	 * @return Key ��Կ
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
		  //ʵ����Des��Կ  
        DESedeKeySpec dks=new DESedeKeySpec(newkeyAry);  
        //ʵ������Կ����  
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //������Կ  
        SecretKey secretKey=keyFactory.generateSecret(dks);  
        return secretKey;  
	}
	
	/**
	 * ��������
	 * @param data ����������
	 * @param key ��Կ
	 * @return byte[] ���ܺ������
	 * */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		//��ԭ��Կ
		Key k=toKey(key);
		//ʵ����
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//ִ�в���
		return cipher.doFinal(data);
	}
	/**
	 * ��������
	 * @param data ����������
	 * @param key ��Կ
	 * @return byte[] ���ܺ������ 
	 * */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		//��ӭ��Կ
		Key k =toKey(key);
		//ʵ����
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.DECRYPT_MODE, k);
		//ִ�в���
		return cipher.doFinal(data);
	}
	
	/**
	 * ��������
	 * @param data ����������
	 * @param key ��Կ
	 * @return byte[] ���ܺ������
	 * */
	public static byte[] encrypt(byte[] data,String key) throws Exception{
		//��ԭ��Կ
		Key k=getKey(key);
		//ʵ����
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//ִ�в���
		return cipher.doFinal(data);
	}
	/**
	 * ��������
	 * @param data ����������
	 * @param key ��Կ
	 * @return byte[] ���ܺ������
	 * */
	public static byte[] decrypt(byte[] data,String key) throws Exception{
		//��ӭ��Կ
		Key k =getKey(key);
		//ʵ����
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.DECRYPT_MODE, k);
		//ִ�в���
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
		
		System.out.println("������Կ:"+byte2HexStr(bytes));
		
		//��ʼ����Կ
		String oldKey = BytetoHexString(bytes);*/
		String oldKey = "8B2713C461577630A0BD495026442D4D";
		
		String str="����3des";
		System.out.println("ԭ�ģ�"+str);
		
		String keyByte = byte2HexStr(str.getBytes());
		//oldKey="6496C14C928B9B9D681021DDD6C4A38D";
		byte[] key = DESedeCoder2.initkey(oldKey);
		System.out.println("oldKey:"+oldKey+", ��Կ����:"+oldKey.length());
		//��������
		byte[] data=DESedeCoder2.encrypt(hexStr2Bytes(keyByte), key);
		System.out.println("���ܺ�"+Base64.encodeBase64String(data));
		//��������
		data=DESedeCoder2.decrypt(data, key);
		System.out.println("���ܺ�"+new String(data));
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
		System.out.println("ԭ�ģ�"+str);
		//��ʼ����Կ
		byte[] keyData = {1,2,3,4,5,6,7,8};
		byte[] keyData2 = {1,2,3,4,5,6,7,8,0,9,11,12,13,14,15,16};
		String oldKey = BytetoHexString(keyData2);
		//oldKey="6496C14C928B9B9D681021DDD6C4A38D";
		System.out.println("oldKey:"+oldKey+", ��Կ����:"+oldKey.length());
		//��������
		byte[] data=DESedeCoder2.encrypt(str.getBytes(), oldKey);
		System.out.println("���ܺ�"+Base64.encodeBase64String(data));
		//��������
		data=DESedeCoder2.decrypt(data, oldKey);
		System.out.println("���ܺ�"+new String(data));
	}
	
}
