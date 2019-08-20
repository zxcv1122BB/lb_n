package com.lb.sys.tools;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * RSA算法加密/解密工具类。
 * 
 */
public class RSAUtilNew {
 
    private final static Log LOGGER = LogFactory.getLog(RSAUtilNew.class);
    /** 算法名称 */
    private final static String ALGORITHOM = "RSA";
    /** 密钥大小 */
    private final static int KEY_SIZE = 1024;
    /** 默认的安全服务提供者 */
    private final static Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
	@SuppressWarnings("unused")
	private static final String RSAPrivateKey = null;
    
    @SuppressWarnings("unused")
	private static RSAUtilNew RSAUtilInstance = null;
 
    private static KeyPairGenerator keyPairGen = null;
    
    static {
		try {
			keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			RSAUtilInstance = new RSAUtilNew();
		}
		catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}    
    
    /**
     * 生成并返回RSA密钥对。
     */
    public static KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(KEY_SIZE, new SecureRandom(UUID.randomUUID().toString().getBytes()));
            return keyPairGen.generateKeyPair();
        } catch (InvalidParameterException ex) {
            LOGGER.error("KeyPairGenerator does not support a key length of " + KEY_SIZE + ".", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.",
                    ex);
        }
        return null;
    }
    
    /**
     * 使用给定的公钥加密给定的字符串(JSON格式)。
     * @param publicKey 给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(PublicKey publicKey, String plaintext) {
        if (publicKey == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(publicKey, data);
            return new String(Hex.encodeHex(en_data));
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }
    
    /**
     * 使用指定的公钥加密数据。
     * 
     * @param publicKey 给定的公钥。
     * @param data 要加密的数据。
     * @return 加密后的数据。
     */
    private static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }
    
    /**
     * 使用给定的公钥加密给定的字符串(JSON格式)。
     * @param privateKey 给定的私钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(PrivateKey privateKey, String plaintext) {
        if (privateKey == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(privateKey, data);
            return new String(Hex.encodeHex(en_data));
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }
    
    /**
     * 使用指定的公钥加密数据。
     * 
     * @param privateKey 给定的私钥。
     * @param data 要加密的数据。
     * @return 加密后的数据。
     */
    private static byte[] encrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }
    
    /**
     * 使用给定的私钥解密给定的字符串。
     * <p />
     * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 私钥不匹配时，返回 {@code null}。
     * 
     * @param privateKey 给定的私钥。
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if (privateKey == null || StringUtils.isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getCause().getMessage()));
        }
        return null;
    }
    
    /**
     * 使用指定的私钥解密数据。
     * 
     * @param privateKey 给定的私钥。
     * @param data 要解密的数据。
     * @return 原数据。
     */
    private static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.DECRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }
    
    /**
     * 使用给定的公钥解密给定的字符串。
     * <p />
     * 若公钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 公钥不匹配时，返回 {@code null}。
     * 
     * @param publicKey 给定的公钥。
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(PublicKey publicKey, String encrypttext) {
        if (publicKey == null || StringUtils.isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(publicKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getCause().getMessage()));
        }
        return null;
    }
    /**
     * 使用指定的公钥解密数据。
     * 
     * @param publicKey 给定的公钥。
     * @param data 要解密的数据。
     * @return 原数据。
     */
    private static byte[] decrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.DECRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }
    

	/**
	 * byte数组转换为二进制字符串,每个字节以","隔开
	 * **/
	public static String conver2HexStr(byte [] b)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<b.length;i++)
		{
			result.append(Long.toString(b[i] & 0xff, 2)+",");
		}
		return result.toString().substring(0, result.length()-1);
	}
	
	/**
	 * 二进制字符串转换为byte数组,每个字节以","隔开
	 * **/
	public static byte[] conver2HexToByte(String hex2Str)
	{
		String [] temp = hex2Str.split(",");
		byte [] b = new byte[temp.length];
		for(int i = 0;i<b.length;i++)
		{
			b[i] = Long.valueOf(temp[i], 2).byteValue();
		}
		return b;
	}
}
