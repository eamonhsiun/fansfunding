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
import com.fansfunding.common.service.TokenService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.PermissionCode;

public class TokenRealm extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;	
	
    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof StatelessToken)||(token instanceof UsernamePasswordToken);
    }
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		if(token instanceof StatelessToken){
			int tokenId =(int)token.getPrincipal();
			Token myToken =tokenService.lookUpTokenById(tokenId);
			if(myToken.getPermission()<PermissionCode.PERMISSION_NORMAL){
				throw new AuthenticationException();
			}
			User user =userService.getUserByName(myToken.getPhone());
			if(user.getToken()==tokenId||(user.getToken_web()==tokenId)){
				return new SimpleAuthenticationInfo(tokenId,tokenId,getName());
			}
		}		
		throw new AuthenticationException();
	}

}
