package com.fansfunding.pay.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.pay.service.PayResultService;
import com.fansfunding.pay.util.AlipayNotify;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("payResult")
public class PayResultController {
	@Autowired
	private PayResultService payResultService;

	@RequestMapping(path="web")
	@ResponseBody
	public Status web(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> params=payResultService.getParams(request);
		//合法性验证
		if(AlipayNotify.verify(params)){
			//判断是否是本商户订单
			if(payResultService.isIllegelOrder(params)){
				//判断该笔订单是否在商户网站中已经做过处理
				if(!payResultService.isFinished(params)){
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					//如果有做过处理，不执行商户的业务程序
					//注意：付款完成后，支付宝系统发送该交易状态通知
					if(payResultService.verify(params)){
						String trade_status = request.getParameter("trade_status");
						if(trade_status.equals("TRADE_FINISHED")){
							System.out.println(trade_status);
						}
						else if (trade_status.equals("TRADE_SUCCESS")){
							System.out.println(trade_status);
						}
						payResultService.finishOrder(params);
						return new Status(true,StatusCode.SUCCESS,"订单完成支付成功",null);
					}
					return new Status(false,StatusCode.FAILD,"订单信息不一致",null);
				}
				return new Status(false,StatusCode.FAILD,"订单已完成",null);
			}
			return new Status(false,StatusCode.FAILD,"非本商户订单",null);
		}
		return new Status(false,StatusCode.FAILD,"验证失败",null);
	}

}
