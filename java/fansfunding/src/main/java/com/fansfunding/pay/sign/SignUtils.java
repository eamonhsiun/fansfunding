package com.fansfunding.pay.sign;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

import com.fansfunding.pay.config.AlipayConfig;

public class SignUtils {
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	public static String sign(Map<String,String> params) {
		String content=SignUtils.create(params);
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(AlipayConfig.privateKey));
			KeyFactory keyf = KeyFactory.getInstance(AlipayConfig.signType);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(AlipayConfig.inputCharset));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String create(Map<String,String> params){
		StringBuffer sb=new StringBuffer();
		params.entrySet().forEach((param)->{
			sb.append(param.getKey()).append("=").append('"').append(param.getValue()).append('"').append("&");
		});
		return sb.deleteCharAt(sb.lastIndexOf("&")).toString();
	}
}