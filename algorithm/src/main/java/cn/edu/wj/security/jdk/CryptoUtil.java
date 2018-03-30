package cn.edu.wj.security.jdk;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * https://www.cnblogs.com/lz2017/p/6917049.html
 * @author jwu
 *
 */
public class CryptoUtil {

	public static void main(String[] args) throws Exception{
		jdkRSA();
	}
	
	//JDK��RSAʵ�� ֧�ֹ�Կ���ܣ�˽Կ���ܺ�˽Կ���ܣ���Կ�������ַ�ʽ��
	public static void jdkRSA() throws Exception {
		String src = "���jdkRSA";
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey=(RSAPublicKey) keyPair.getPublic();           //��Կ
        RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) keyPair.getPrivate();       //˽Կ
        System.out.println("public key:"+Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("private key:"+Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        
        //˽Կ���ܣ���Կ����--����
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("RSA˽Կ���ܣ���Կ����--����:"+Base64.encodeBase64String(result));
        
        //˽Կ���ܣ���Կ����--����
        X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        PublicKey publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        result = cipher.doFinal(result);
        System.out.println("RSA˽Կ���ܣ���Կ����--����:"+new String(result));
        
        //��Կ���ܣ�˽Կ����--����
        x509EncodedKeySpec=new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        result = cipher.doFinal(src.getBytes());
        System.out.println("RSA��Կ���ܣ�˽Կ����--����:"+Base64.encodeBase64String(result));
        
        //��Կ���ܣ�˽Կ����--����
        pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        privateKey =keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        result=cipher.doFinal(result);
        System.out.println("RSA��Կ���ܣ�˽Կ����--����:"+new String(result));
	}
	
	//���ڿ���ĶԳƼ����㷨������ʵ�Ƕ�֮ǰ���㷨�İ�װ������˵MD5��DES����������ǵ��Ƕ�MD5��DES��װ��PBE�㷨�������������͵�PBE����
	//������������׻�˵�����룬PBE����һ��salt���Σ��ĸ���ξ��Ǹ�����
	public static void jdkPBE() throws Exception{
		String src = "���PBE";
		 //��ʼ����
        SecureRandom random=new SecureRandom();
        byte[] salt = random.generateSeed(8);   //ָ��Ϊ8λ���� ���ξ��Ǹ����룬ͨ����Ӹ��������Ӱ�ȫ��
        
        //�������Կ
        String password="lynu";              //����
        PBEKeySpec pbeKeySpec=new PBEKeySpec(password.toCharArray());
        SecretKeyFactory factory=SecretKeyFactory.getInstance("PBEWITHMD5andDES");
        Key key=factory.generateSecret(pbeKeySpec);  //��Կ
        
        //����  �����淶����һ���������Σ��ڶ����ǵ�������������ɢ�к�����ε�����
        PBEParameterSpec pbeParameterSpec=new PBEParameterSpec(salt, 100);
        Cipher cipher=Cipher.getInstance("PBEWITHMD5andDES");
        cipher.init(Cipher.ENCRYPT_MODE, key,pbeParameterSpec);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdk PBE����: "+org.apache.commons.codec.binary.Base64.encodeBase64String(result));
        
        
        //����
        cipher.init(Cipher.DECRYPT_MODE, key,pbeParameterSpec);
        result = cipher.doFinal(result);
        System.out.println("jdk PBE����: "+new String(result));
	}
	
	public static void jdkAES() throws Exception{
		String src = "���AES";
		
		//����key
        KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
        keyGenerator.init(new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key1 = secretKey.getEncoded();
        
        //keyת��Ϊ��Կ
        Key key2 = new SecretKeySpec(key1, "AES");
        
        //����
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5padding");
        cipher.init(Cipher.ENCRYPT_MODE, key2);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdkAES����: "+Hex.toHexString(result));  //ת��Ϊʮ������
        
        //����
        cipher.init(Cipher.DECRYPT_MODE, key2);
        result = cipher.doFinal(result);
        System.out.println("jdkAES����: "+new String(result));  //ת���ַ���
	}
	
	public static void bcAES() throws Exception{
		String src = "���AES";
		
		Security.addProvider(new BouncyCastleProvider());
		//����key
        KeyGenerator keyGenerator=KeyGenerator.getInstance("AES","BC");
        keyGenerator.init(128);
        
        System.out.println(keyGenerator.getProvider());
        
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key1 = secretKey.getEncoded();
        
        //keyת��Ϊ��Կ
        Key key2 = new SecretKeySpec(key1, "AES");
        
        //����
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5padding");
        cipher.init(Cipher.ENCRYPT_MODE, key2);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdkAES����: "+Hex.toHexString(result));  //ת��Ϊʮ������
        
        //����
        cipher.init(Cipher.DECRYPT_MODE, key2);
        result = cipher.doFinal(result);
        System.out.println("jdkAES����: "+new String(result));  //ת���ַ���
	}
	
}
