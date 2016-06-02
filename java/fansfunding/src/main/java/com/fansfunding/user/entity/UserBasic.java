package com.fansfunding.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserBasic {
	@JsonProperty
	private int id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String nickname;
	@JsonProperty
	private String password;
	@JsonProperty
	private String phone;
	@JsonProperty
	private String imei;
	@JsonProperty
	private String token;
	
	public UserBasic() {
	}

	
	
	public UserBasic(User user) {
		super();
		this.id = user.getId();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.phone = user.getPhone();
		this.imei = user.getIMEI();
		this.token = user.getToken();
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getIMEI() {
		return imei;
	}
	public void setIMEI(String iMEI) {
		imei = iMEI;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
