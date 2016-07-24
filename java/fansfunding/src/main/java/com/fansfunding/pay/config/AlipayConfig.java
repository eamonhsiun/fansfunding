package com.fansfunding.pay.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 支付宝账号配置
 * 合作者ID以及私钥、公钥硬编码至程序中
 * 回调地址可配置
 * @author w-angler
 *
 */
public class AlipayConfig {
	private AlipayConfig(){
		throw new RuntimeException("You can not new an instance of this class!");
	}
	/**
	 * 合作身份者ID,签约账号,以2088开头由16位纯数字组成的字符串
	 */
	public static final String partner = "2088901426909794";
	public static final String sellerId = partner;
	/**
	 * 商户的私钥,需要PKCS8格式
	 */
	public static final String privateKey = 
			  "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMrmk4ZRKGA4R/IL"
			+ "fYzZfN3Yw0cK1oRRC5p3g6XA4xleyAb5HAUFnmcjjQrsWFxibEDbauWpZ8I2/gCD"
			+ "p//1T62/6Gr24TdtVvVJcPv53usHOx2wmMfSfpkbzBFm06fWal2Xionp4FLrC4O8"
			+ "RaJlqq3U+uVC4YAHq1DWpp/2QG3TAgMBAAECgYAl+Nr5Ey00PLQyNzZK6tT0i8GY"
			+ "BQ46exRp1x4bIiseL1/N9kbpJbEMalnWBn9O4tiRlr69tmOFtFb676i8PUOo0mgn"
			+ "wLAe0n8lzxqjH+Z31fYfG/vBu52KAdxyQl9Q0bK1zVRM+eQ35zT/gEp+ci4NZiyH"
			+ "CCdCh0j6F6cHHQdRUQJBAORQXJEj3QtZnNQfkCv0jSkkysOCVNSwISqt76dsL9Hy"
			+ "awqpfar6l2PwLco62lh/b9NuGmzrJX5ucySdbcir9nsCQQDjgU1PY1HanKMnxIxq"
			+ "rPyH0oy9kGRExFpw2OqbSG10quL1MnxOPAtQZvu/8s33E1TLQyvLdRj26WOu4bXX"
			+ "CLKJAkEAggqDrueOb3iLKwcPRvnzrQuXseP6DWqEeZq4ddPZKOu6rEf9m/U/ZVzX"
			+ "t+ftlZqARxzfUEpfALRIQQKqFTJdIQJBAIP+NeN0ZT4nmMVu1GTj3lPTBZLJ3lLD"
			+ "dRF52IulNtP0fV7zHompbosEZNVVwsexsIWJgDQ3yPcgK5SAIacTxbkCQQC5aScc"
			+ "SfUpxYkWTjzK7EUkiQtNWWTctVnYihOlZTnbtB8mR+bmo7UCZIAsaBGpJb//sEuY"
			+ "wGJrjFtxvOdGljvb";
	/**
	 * 支付宝的公钥
	 */
	public static final String alipayPublicKey  = 
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/"
			+ "y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPX"
			+ "mKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23M"
			+ "ML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLk"
			+ "xpLQIDAQAB";
	/**
	 * 服务器异步通知页面路径, 需http://格式的完整路径,不能加?id=123这类自定义参数,必须外网可以正常访问
	 */
	public static String webNotifyUrl = null;
	/**
	 * 页面跳转同步通知页面路径,需http://格式的完整路径,不能加?id=123这类自定义参数,必须外网可以正常访问
	 */
	public static String webReturnUrl = null;
	/**
	 *移动支付异步通知地址 
	 */
	public static String mobileNotifyUrl=null;
	/**
	 * 签名方式
	 */
	public static final String signType = "RSA";
	/**
	 * 字符编码格式 目前支持 gbk 或 utf-8
	 */
	public static final String inputCharset = "utf-8";
	/**
	 * 支付类型 ，无需修改
	 */
	public static final String paymentType = "1";
	/**
	 * 调用的接口名，无需修改
	 */
	public static final String webService = "create_direct_pay_by_user";
	public static final String mobileService="mobile.securitypay.pay";
	/*
	 * 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可
	 */
	
	/**
	 * 防钓鱼时间戳  若要使用请调用类文件AlipaySubmit中的queryTimestamp函数
	 */
	public static String antiPhishingKey;
	/**
	 * 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	 */
	public static String exterInvokeIp;
	static{
		Properties prop=new Properties();
		Resource resource=new ClassPathResource("alipay.properties");
		try {
			prop.load(resource.getInputStream());
			webNotifyUrl=prop.getProperty("webNotifyUrl");
			webReturnUrl=prop.getProperty("webReturnUrl");
			mobileNotifyUrl=prop.getProperty("mobileNotifyUrl");
			antiPhishingKey=prop.getProperty("antiPhishingKey");
			exterInvokeIp=prop.getProperty("exterInvokeIp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

