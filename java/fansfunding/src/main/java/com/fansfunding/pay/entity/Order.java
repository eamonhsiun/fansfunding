package com.fansfunding.pay.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	private Integer id;
	private String orderNo;
	private String tradeNo;
	private Integer userId;
	private Integer projectId;
	private Integer addressId;
	private Integer feedbackId;
	private String subject;
	private String totalFee;
	private String tradeStatus;
	private String buyerEmail;
	private String buyerId;
	private String sellerId;
	private Character delFlag;
	private Date createTime;
	private Date returnTime;
	private Date notifyTime;
	private String payMode;
}
