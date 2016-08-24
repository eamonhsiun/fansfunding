<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>支付结果</title>
</head>
<body>
<script type="text/javascript">
window.location.href='http://crowdfunding.immortalfans.com/order.html?categoryId=${result.get("categoryId")}&projectId=${result.get("projectId")}&feedbackId=${result.get("feedbackId")}&orderStatus=${result.get("orderStatus")}&orderNo=${result.get("orderNo")}';
</script>
</body>
</html>