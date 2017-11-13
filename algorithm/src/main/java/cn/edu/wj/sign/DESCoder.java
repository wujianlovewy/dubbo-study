package cn.edu.wj.sign;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESCoder {
	/**
	 * ��Կ�㷨
	 * java֧��56λ��Կ��bouncycastle֧��64λ
	 * */
	public static final String KEY_ALGORITHM="DES";
	
	/**
	 * ����/�����㷨/����ģʽ/��䷽ʽ
	 * */
	public static final String CIPHER_ALGORITHM="DES/ECB/PKCS5Padding";
	
	/**
	 * 
	 * ������Կ��java6ֻ֧��56λ��Կ��bouncycastle֧��64λ��Կ
	 * @return byte[] ��������Կ
	 * */
	public static byte[] initkey() throws Exception{
		
		//ʵ������Կ������
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		//��ʼ����Կ������
		kg.init(56);
		//������Կ
		SecretKey secretKey=kg.generateKey();
		//��ȡ��������Կ������ʽ
		return secretKey.getEncoded();
	}
	/**
	 * ת����Կ
	 * @param key ��������Կ
	 * @return Key ��Կ
	 * */
	public static Key toKey(byte[] key) throws Exception{
		//ʵ����Des��Կ
		DESKeySpec dks=new DESKeySpec(key);
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
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String str="DES";
		System.out.println("ԭ�ģ�"+str);
		//��ʼ����Կ
		byte[] key=DESCoder.initkey();
		
		System.out.println("��Կ��"+Base64.encodeBase64String(key)+", ��Կ����:"+key.length);
		//��������
		byte[] data=DESCoder.encrypt(str.getBytes(), key);
		System.out.println("���ܺ�"+Base64.encodeBase64String(data));
		//��������
		data=DESCoder.decrypt(data, key);
		System.out.println("���ܺ�"+new String(data));
	}
}
