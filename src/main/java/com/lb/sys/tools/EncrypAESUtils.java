package com.lb.sys.tools;


import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * 对称加密-AES
 */
public class EncrypAESUtils {

	private final byte[] DESIV = new byte[] { 0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef };// 向量
	 
	private AlgorithmParameterSpec iv = null;// 加密算法的参数接口
	private Key key = null;
	//自定义密钥
	private static String SECRET_KEY = "9ba45bfd500642328ec03ad8ef1b6e75";
	//编码格式
	private static String CHARSET = "utf-8";
	
	/**
	 * 初始化
	 * @throws Exception
	 */
	public EncrypAESUtils() throws Exception {
		DESKeySpec keySpec = new DESKeySpec(SECRET_KEY.getBytes(CHARSET));// 设置密钥参数
		iv = new IvParameterSpec(DESIV);// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		key = keyFactory.generateSecret(keySpec);// 得到密钥对象
	}
	
	/**
	 * 加密
	 * @author ershuai
	 * @date 2017年4月19日 上午9:40:53
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String encode(String data) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
		sun.misc.BASE64Encoder  decoder = new sun.misc.BASE64Encoder();
		return decoder.encode(pasByte);
	}
	
	/**
	 * 解密
	 * @author ershuai
	 * @date 2017年4月19日 上午9:41:01
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String decode(String data) throws Exception {
		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, iv);
		sun.misc.BASE64Decoder  base64Decoder = new sun.misc.BASE64Decoder();
		byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
		return new String(pasByte, "UTF-8");
	}
	
	public static void main(String[] args) {
		try {
			EncrypAESUtils encry = new EncrypAESUtils();
			String test = "103.232.85.210";
			String encode = encry.encode(test);
			System.out.println("加密："+encode);
			System.err.println("解密："+encry.decode("ePEsBsyXglo=" ));
			System.err.println("解密："+encry.decode("5Pux3peC+NpIE2rgUecR0ls2lYUSVrA704q9w9WnJ1aJguZmhwzB+jrlq85nMrPU9ZiU8Aim2lUfp4vM4VNOsKiTx9VRJbM0b60Uv5NDn3z3p4/ltKTjuvtSd0eSq+spwyqAmYLzp1a7rAqX9EUfC1tGAEGM353/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
