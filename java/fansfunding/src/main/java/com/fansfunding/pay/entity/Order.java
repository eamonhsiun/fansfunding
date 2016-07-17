package com.fansfunding.pay.entity;

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
	private Integer feedbackId;
	private String subject;
	private String totalFee;
	private String tradeStatus;
	private String buyerEmail;
	private String buyerId;
	private String sellerId;
	private Character delFlag;
}
