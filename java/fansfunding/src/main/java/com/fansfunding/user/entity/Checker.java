package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checker {
	private int id;
	
	private String IMEI;
	
	private int checknum;
	
	private Date request_time;
	
	private String phone;
	
	private String token;
	
}


