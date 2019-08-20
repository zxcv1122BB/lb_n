/**
 * 
 */
package com.lb.sys.tools;

import java.security.PrivateKey;
import java.util.Map;

import org.bouncycastle.jce.provider.JCERSAPrivateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @describe 
 */
public class ControlTools {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControlTools.class);
	
	private ControlTools() {};
	
	private static JdbcTemplate JDBC_TEMPLATE;
	
	//private static PrivateKey PRIVATE_KEY;
	
	/*static {
		JDBC_TEMPLATE = SpringUtil.getBean(JdbcTemplate.class);
	}*/
	/**
	 * rsa私钥解密
	 * */
	public static synchronized String decrypt(String encryptString) {
		PrivateKey privateKey = resetRsaKey();
		String rsaData = null;
		if(privateKey != null) {
			if(encryptString.contains(",")) {
				StringBuilder sbr = new StringBuilder();
				String[] strArrays = encryptString.split(",");
				for (String str : strArrays) {
					String data = RSAUtilNew.decryptString(privateKey, str);
					if(data != null) {
						sbr.append(data);
					}
				}
				rsaData = sbr.toString();
			}else
				rsaData = RSAUtilNew.decryptString(privateKey, encryptString);
		}
		return rsaData;
	}
	/**
	 * rsa私钥解密解码
	 * */
	public static synchronized String unescapeDecrypt(String encryptString) {
		String rsaData = decrypt(encryptString);
		rsaData = EscapeToolBox.unescape(rsaData);
		return rsaData;
	}
	/**
	 * rsa私钥加密
	 * */
	private static synchronized String encrypt(String data) {
		if(StringUtil.isBlank(data)) {
			return null;
		}
		PrivateKey privateKey = resetRsaKey();
		if(privateKey != null) {
			if(data.length() > 128) {
				StringBuilder sbr = new StringBuilder();
				for(int i = 0 ; i < data.length(); i+=128) {
					sbr.append(",");
					if(data.length() < i + 128) {
						sbr.append(RSAUtilNew.encryptString(privateKey, data.substring(i, data.length())));
					}else
						sbr.append(RSAUtilNew.encryptString(privateKey, data.substring(i, i + 128)));
				}
				return sbr.substring(1);
			}else
				return RSAUtilNew.encryptString(privateKey, data);
		}
		LOGGER.error("获取私钥失败");
		return null;
	}
	/**
	 * rsa公钥转码加密
	 * */
	public static String escapeEncrypt(String data) {
		if(StringUtil.isBlank(data)) {
			return null;
		}
		data = EscapeToolBox.escape(data);
		return encrypt(data);
	}
	/**
	 * rsa公钥转码加密
	 * */
	public static String escapeEncrypt(String key , String data) {
		if(StringUtil.isBlank(data) || StringUtil.isBlank(key)) {
			return null;
		}
		PrivateKey privateKey = ProtostuffUtil.deserializer(BytesHexStrTranslate.toBytes(key), JCERSAPrivateKey.class);
		if(privateKey != null) {
			data = EscapeToolBox.escape(data);
			if(data.length() > 128) {
				StringBuilder sbr = new StringBuilder();
				for(int i = 0 ; i < data.length(); i+=128) {
					sbr.append(",");
					if(data.length() < i + 128) {
						sbr.append(RSAUtilNew.encryptString(privateKey, data.substring(i, data.length())));
					}else
						sbr.append(RSAUtilNew.encryptString(privateKey, data.substring(i, i + 128)));
				}
				return sbr.substring(1);
			}else
				return RSAUtilNew.encryptString(privateKey, data);
		}
		LOGGER.error("获取私钥失败");
		return null;
	}
	
	private static PrivateKey resetRsaKey() {
		if(JDBC_TEMPLATE == null) {
			JDBC_TEMPLATE = SpringUtil.getBean(JdbcTemplate.class);
		}
		if(JDBC_TEMPLATE != null) {
			StringBuilder sql = new StringBuilder("SELECT ");
			sql.append("`values`")
			.append("FROM")
			.append("  `properties` ")
			.append("WHERE properties.`keys`='rsaConfig' AND properties.`child_node_keys`='private_key' ") 
					.append("LIMIT 1");
			Map<String, Object> map = null;
			try {
				map = JDBC_TEMPLATE.queryForMap(sql.toString());
			} catch (Exception e) {
				LOGGER.error("数据库properties值不存在：" + e.getMessage());
			}
			if(map != null && map.get("values") != null) {
				//PRIVATE_KEY = (RSAPrivateKey) SerializeUtils.deSerialize(BytesHexStrTranslate.toBytes(map.get("privateKey").toString()));
				return ProtostuffUtil.deserializer(BytesHexStrTranslate.toBytes(map.get("values").toString()), JCERSAPrivateKey.class);
			}
		}
		return null;
	}
}
