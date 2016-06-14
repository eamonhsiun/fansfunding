package com.fansfunding.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.FundFlowDao;
import com.fansfunding.project.entity.FundFlow;

@Service
public class FundFlowService {
	@Autowired
	private FundFlowDao fundFlowDao;
	
	/**
	 * 添加
	 * @param fundFlow
	 */
	public void add(FundFlow fundFlow){
		fundFlowDao.insert(fundFlow);
	}
	/**
	 * 支付
	 * @param fundFlow
	 * @return
	 */
	public boolean pay(FundFlow fundFlow){
		
		return true;
	}
}
