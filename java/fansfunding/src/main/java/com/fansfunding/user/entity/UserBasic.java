package com.fansfunding.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasic {
	private int id;
	private String name;
	private String nickname;
	private String password;
	private String phone;
	private String IMEI;
	private int token;
	
	public UserBasic(User user) {
		super();
		this.id = user.getId();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.phone = user.getPhone();
		this.IMEI = user.getIMEI();
		this.token = user.getToken();
	}

}
