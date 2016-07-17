package com.fansfunding.common.entity;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.fansfunding.common.interceptor.StatelessToken;
import com.fansfunding.user.service.UserService;

public class TokenRealm extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof StatelessToken)||(token instanceof UsernamePasswordToken);
    }
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		String username=(String)principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		//Subject subject = SecurityUtils.getSubject();
		System.err.println("======================");
		//subject.checkRole("admin");
		//TODO:NOT FINISHED!!!
//		authorizationInfo.setRoles(userService.findRoles(username));
//		authorizationInfo.setStringPermissions(userService.findPermissions(username));;
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		if(token instanceof StatelessToken){
			//TODO:NOT FINISHED
			int tokenId =(int)token.getPrincipal();
			return new SimpleAuthenticationInfo(tokenId,tokenId,getName());
		}		
		return null;
	}

}
