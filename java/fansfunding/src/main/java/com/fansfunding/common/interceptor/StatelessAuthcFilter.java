package com.fansfunding.common.interceptor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

public class StatelessAuthcFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		String token = request.getParameter("token");

		try {
			// TODO:NOT FINISHED!!!
			StatelessToken upt = new StatelessToken(token);
			getSubject(request, response).login(upt);
		} catch (Exception e) {
			// onLoginFail(response); //6、登录失败
			return false;
		}

		return true;

	}

}
