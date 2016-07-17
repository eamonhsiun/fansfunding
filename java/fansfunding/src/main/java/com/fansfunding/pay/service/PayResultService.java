package com.fansfunding.pay.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.pay.entity.Order;
import com.fansfunding.pay.dao.OrderDao;

@Service
public class PayResultService {

	@Autowired
	private OrderDao orderDao;

	/**
	 * 分析请求，获取参数
	 * @param request
	 * @return
	 */
	public Map<String,String> getParams(HttpServletRequest request){
		Map<String,String> params = new HashMap<String,String>();
		request.getParameterMap().entrySet().forEach((entrySet)->{
			String name =entrySet.getKey();
			String[] values = entrySet.getValue();
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		});
		return params;
	}

	/**
	 * 定单完成，在知乎完成后由支付宝配置的return_url的接口调用，保存订单信息
	 * @param callbackParams 回调后的请求参数
	 */
	public void finishOrder(Map<String,String> callbackParams){
		String orderNo=callbackParams.get("out_trade_no");
		Order order=orderDao.selectByOrderNo(orderNo);
		order.setTradeNo(callbackParams.get("trade_no"));
		order.setTradeStatus(callbackParams.get("trade_status"));
		order.setBuyerEmail(callbackParams.get("buyer_email"));
		order.setBuyerId(callbackParams.get("buyer_id"));
		orderDao.updateByPrimaryKey(order);
	}
	/**
	 * 
	 * @param callbackParams 回调后的请求参数
	 * @return
	 */
	public boolean verify(Map<String,String> callbackParams){
		String orderNo=callbackParams.get("out_trade_no");
		Order order=orderDao.selectByOrderNo(orderNo);
		if(order!=null){
			String sellerId=callbackParams.get("seller_id");
			String total_fee=callbackParams.get("total_fee");
			if(order.getSellerId().equals(sellerId)&&order.getTotalFee().equals(total_fee)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 订单是否已经完成
	 * @param callbackParams 回调后的请求参数
	 * @return
	 */
	public boolean isFinished(Map<String,String> callbackParams){
		String orderNo=callbackParams.get("out_trade_no");
		Order order=orderDao.selectByOrderNo(orderNo);
		return order.getTradeStatus()!=null;
	}
	/**
	 * 是否是合法的订单
	 * @param callbackParams 回调后的请求参数
	 * @return
	 */
	public boolean isIllegelOrder(Map<String,String> callbackParams){
		String orderNo=callbackParams.get("out_trade_no");
		Order order=orderDao.selectByOrderNo(orderNo);
		return order!=null;
	}
}
