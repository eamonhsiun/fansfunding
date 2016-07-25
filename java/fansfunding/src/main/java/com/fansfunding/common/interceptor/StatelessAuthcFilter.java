package com.fansfunding.common.interceptor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			StatelessToken upt = new StatelessToken(token);
			getSubject(request, response).login(upt);
		} catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(mapper.writeValueAsString(
					new Status(false, StatusCode.PERMISSION_LOW, "权限过低，请求已被拦截", null)));
			return false;
		}
		return true;
	}

}
