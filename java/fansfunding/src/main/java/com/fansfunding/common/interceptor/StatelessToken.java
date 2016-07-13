package com.fansfunding.common.interceptor;

import org.apache.shiro.authc.AuthenticationToken;

import com.fansfunding.utils.encrypt.AESUtils;

public class StatelessToken implements AuthenticationToken {
	private static final long serialVersionUID = 2205056186928610401L;
	
	int tokenId;

	public StatelessToken(String token) throws Exception {
		tokenId=Integer.parseInt(AESUtils.Decrypt(token, AESUtils.ENCRYPT_KEY));
	}
	
	
	@Override
	public Object getPrincipal() {
		return tokenId;
	}
	@Override
	public Object getCredentials() {
		return tokenId;
	}

}
