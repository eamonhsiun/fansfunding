<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fansfunding.pay.util.AlipaySubmit" %>
<%@page import="java.util.Map" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>FAN$众筹网页支付</title>
	</head>
	<body>
	<%
		out.println(AlipaySubmit.buildRequest((Map<String,String>)request.getAttribute("params"),"get","确认"));
	%>
	</body>
</html>
