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
	
	//JDK的RSA实现 支持公钥加密，私钥解密和私钥加密，公钥解密两种方式：
	public static void jdkRSA() throws Exception {
		String src = "你好jdkRSA";
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey=(RSAPublicKey) keyPair.getPublic();           //公钥
        RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) keyPair.getPrivate();       //私钥
        System.out.println("public key:"+Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("private key:"+Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        
        //私钥加密，公钥解密--加密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("RSA私钥加密，公钥解密--加密:"+Base64.encodeBase64String(result));
        
        //私钥加密，公钥解密--解密
        X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        PublicKey publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        result = cipher.doFinal(result);
        System.out.println("RSA私钥加密，公钥解密--解密:"+new String(result));
        
        //公钥加密，私钥解密--加密
        x509EncodedKeySpec=new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        result = cipher.doFinal(src.getBytes());
        System.out.println("RSA公钥加密，私钥解密--加密:"+Base64.encodeBase64String(result));
        
        //公钥加密，私钥解密--解密
        pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        keyFactory=KeyFactory.getInstance("RSA");
        privateKey =keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        result=cipher.doFinal(result);
        System.out.println("RSA公钥加密，私钥解密--解密:"+new String(result));
	}
	
	//基于口令的对称加密算法（它其实是对之前的算法的包装，比如说MD5和DES，我这里就是的是对MD5和DES包装的PBE算法，还有其他类型的PBE），
	//口令就是我们俗话说的密码，PBE中有一个salt（盐）的概念，盐就是干扰码
	public static void jdkPBE() throws Exception{
		String src = "你好PBE";
		 //初始化盐
        SecureRandom random=new SecureRandom();
        byte[] salt = random.generateSeed(8);   //指定为8位的盐 （盐就是干扰码，通过添加干扰码增加安全）
        
        //口令和密钥
        String password="lynu";              //口令
        PBEKeySpec pbeKeySpec=new PBEKeySpec(password.toCharArray());
        SecretKeyFactory factory=SecretKeyFactory.getInstance("PBEWITHMD5andDES");
        Key key=factory.generateSecret(pbeKeySpec);  //密钥
        
        //加密  参数规范，第一个参数是盐，第二个是迭代次数（经过散列函数多次迭代）
        PBEParameterSpec pbeParameterSpec=new PBEParameterSpec(salt, 100);
        Cipher cipher=Cipher.getInstance("PBEWITHMD5andDES");
        cipher.init(Cipher.ENCRYPT_MODE, key,pbeParameterSpec);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdk PBE加密: "+org.apache.commons.codec.binary.Base64.encodeBase64String(result));
        
        
        //解密
        cipher.init(Cipher.DECRYPT_MODE, key,pbeParameterSpec);
        result = cipher.doFinal(result);
        System.out.println("jdk PBE解密: "+new String(result));
	}
	
	public static void jdkAES() throws Exception{
		String src = "你好AES";
		
		//生成key
        KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
        keyGenerator.init(new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key1 = secretKey.getEncoded();
        
        //key转换为密钥
        Key key2 = new SecretKeySpec(key1, "AES");
        
        //加密
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5padding");
        cipher.init(Cipher.ENCRYPT_MODE, key2);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdkAES加密: "+Hex.toHexString(result));  //转换为十六进制
        
        //解密
        cipher.init(Cipher.DECRYPT_MODE, key2);
        result = cipher.doFinal(result);
        System.out.println("jdkAES解密: "+new String(result));  //转换字符串
	}
	
	public static void bcAES() throws Exception{
		String src = "你好AES";
		
		Security.addProvider(new BouncyCastleProvider());
		//生成key
        KeyGenerator keyGenerator=KeyGenerator.getInstance("AES","BC");
        keyGenerator.init(128);
        
        System.out.println(keyGenerator.getProvider());
        
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key1 = secretKey.getEncoded();
        
        //key转换为密钥
        Key key2 = new SecretKeySpec(key1, "AES");
        
        //加密
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5padding");
        cipher.init(Cipher.ENCRYPT_MODE, key2);
        byte[] result = cipher.doFinal(src.getBytes());
        System.out.println("jdkAES加密: "+Hex.toHexString(result));  //转换为十六进制
        
        //解密
        cipher.init(Cipher.DECRYPT_MODE, key2);
        result = cipher.doFinal(result);
        System.out.println("jdkAES解密: "+new String(result));  //转换字符串
	}
	
}
