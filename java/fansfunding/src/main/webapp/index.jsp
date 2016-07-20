<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付宝即时到账交易接口测试</title>
</head>

<style>
    html,body {
        width:100%;
        min-width:1200px;
        height:auto;
        padding:0;
        margin:0;
        font-family:"微软雅黑";
        background-color:#242736
    }
    .content {
        width:100%;
        min-width:1200px;
        height:660px;
        background-color:#fff;      
    }
    .alipayform {
        width:800px;
        margin:0 auto;
        height:600px;
        border:1px solid #0ae
    }
    .element {
        width:600px;
        height:80px;
        margin-left:100px;
        font-size:20px
    }
    .alisubmit {
        width:400px;
        height:40px;
        border:0;
        background-color:#0ae;
        font-size:16px;
        color:#FFF;
        cursor:pointer;
        margin-left:170px
    }
</style>
<body>
    <div class="content">
        <form action="pay/web" class="alipayform" method="POST" target="_blank">
        	<div>
        		<input name="feedbackId" value="1" hidden="hidden"/>
        	</div>
        	<div>
        		<input name="userId" value="10000023" hidden="hidden"/>
        	</div>
            <div class="element">
                <input style="margin-top:200px" type="submit" class="alisubmit" value ="确认支付">
            </div>
        </form>
    </div>
</body>
</html>