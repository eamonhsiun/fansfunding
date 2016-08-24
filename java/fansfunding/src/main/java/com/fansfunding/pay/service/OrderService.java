package com.fansfunding.pay.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.pay.config.AlipayConfig;
import com.fansfunding.pay.dao.OrderDao;
import com.fansfunding.pay.entity.Order;
import com.fansfunding.pay.sign.SignUtils;
import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.entity.Feedback;
import com.fansfunding.utils.DateUtil;

@Service
public class OrderService {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private FeedbackDao feedbackDao;
	@Autowired
	private OrderDao orderDao;
	
	/**
	 * 是否可以支付
	 * @param feedbackId
	 * @return
	 */
	public boolean isPayable(int feedbackId){
		Feedback f=feedbackDao.selectByPrimaryKey(feedbackId);
		if(f.getCeiling()==-1){
			return true;
		}
		return orderDao.selectByFeedbackId(feedbackId).size()<f.getCeiling();
	}
	/**
	 * 构建web支付订单
	 * @param feedbackId 回馈方式id
	 * @param userId 用户id
	 * @return
	 */
	public Map<String,String> buildWebOrder(int feedbackId,int userId,int addressId){
		Feedback feedback=feedbackDao.selectByPrimaryKey(feedbackId);
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = "p"+feedback.getProjectId()+"f"+feedback.getId()+"t"+DateUtil.getOrderNum();
		//订单名称，必填
		String subject = projectDao.selectByProjectId(feedback.getProjectId()).getName()+"-"+feedback.getTitle();
		//付款金额，必填
		String total_fee = String.format("%.2f", feedback.getLimitation());
		//商品描述，可空
		String body =feedback.getDescription();

		//保存订单
		Order order=new Order();
		order.setOrderNo(out_trade_no);
		order.setSubject(subject);
		order.setTotalFee(String.format("%.2f", feedback.getLimitation()));
		order.setProjectId(projectDao.selectByProjectId(feedback.getProjectId()).getId());
		order.setFeedbackId(feedbackId);
		order.setUserId(userId);
		order.setSellerId(AlipayConfig.sellerId);
		order.setPayMode("web");
		order.setAddressId(addressId);
		this.saveOrder(order);

		//请求参数
		Map<String,String> params=new HashMap<>();
		params.put("service", AlipayConfig.webService);
		params.put("partner", AlipayConfig.partner);
		params.put("seller_id", AlipayConfig.sellerId);
		params.put("_input_charset", AlipayConfig.inputCharset);
		params.put("payment_type", AlipayConfig.paymentType);
		params.put("notify_url", AlipayConfig.webNotifyUrl);
		params.put("return_url", AlipayConfig.webReturnUrl);
		params.put("anti_phishing_key", AlipayConfig.antiPhishingKey);
		params.put("exter_invoke_ip", AlipayConfig.exterInvokeIp);
		params.put("out_trade_no", out_trade_no);
		params.put("subject", subject);
		params.put("total_fee", total_fee);
		params.put("body", body);
		return params;
	}

	/**
	 * 构建移动支付支付订单
	 * @param feedbackId 回馈方式id
	 * @param userId 用户id
	 * @return
	 */
	private Map<String,String> buildMobileOrder(int feedbackId,int userId,int addressId){
		Feedback feedback=feedbackDao.selectByPrimaryKey(feedbackId);
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = "p"+feedback.getProjectId()+"f"+feedback.getId()+"t"+DateUtil.getOrderNum();
		//订单名称，必填
		String subject = projectDao.selectByProjectId(feedback.getProjectId()).getName()+"-"+feedback.getTitle();
		//付款金额，必填
		String total_fee = String.format("%.2f", feedback.getLimitation());
		//商品描述，可空
		String body =feedback.getDescription();

		//保存订单
		Order order=new Order();
		order.setOrderNo(out_trade_no);
		order.setSubject(subject);
		order.setTotalFee(String.format("%.2f", feedback.getLimitation()));
		order.setProjectId(projectDao.selectByProjectId(feedback.getProjectId()).getId());
		order.setFeedbackId(feedbackId);
		order.setUserId(userId);
		order.setSellerId(AlipayConfig.sellerId);
		order.setPayMode("mobile");
		order.setAddressId(addressId);
		this.saveOrder(order);

		//请求参数
		Map<String,String> params=new HashMap<>();
		params.put("service", AlipayConfig.mobileService);
		params.put("partner", AlipayConfig.partner);
		params.put("seller_id", AlipayConfig.sellerId);
		params.put("_input_charset", AlipayConfig.inputCharset);
		params.put("payment_type", AlipayConfig.paymentType);
		params.put("notify_url", AlipayConfig.mobileNotifyUrl);
		params.put("out_trade_no", out_trade_no);
		params.put("subject", subject);
		params.put("total_fee", total_fee);
		params.put("body", body);
		params.put("it_b_pay", "30m");
		return params;
	}
	/**
	 * 保存订单
	 * @param order
	 */
	private void saveOrder(Order order){
		orderDao.insert(order);
	}
	/**
	 * 生成签名后的订单信息
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public Map<String,String> sign(int feedbackId,int userId,int addressId) throws UnsupportedEncodingException{
		Map<String,String> order=new HashMap<>();
		StringBuffer sb=new StringBuffer();
		Map<String,String> params=this.buildMobileOrder(feedbackId, userId,addressId);
		params.entrySet().forEach((param)->{
			sb.append(param.getKey()).append("=\"").append(param.getValue()).append("\"&");
		});
		sb.append("sign=\"").append(URLEncoder.encode(SignUtils.sign(params),"UTF-8")).append("\"&")
		.append("sign_type=\"").append(AlipayConfig.signType).append("\"");
		order.put("orderNo", params.get("out_trade_no"));
		order.put("signedOrder", sb.toString());
		return order;
	}
}
