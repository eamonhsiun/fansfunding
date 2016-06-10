package com.fansfunding.user.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends UserBasic{

	@JsonProperty
	private int is_red;
	@JsonProperty
	private String head;
	@JsonProperty
	private String email;
	@JsonProperty
	private String create_by;
	@JsonProperty
	private Date create_time;
	@JsonProperty
	private String update_by;
	@JsonProperty
	private Date update_time;
	@JsonProperty
	private String remark;
	@JsonProperty
	private char del_flag;
	@JsonProperty
	private String token_wx;
	@JsonProperty
	private String token_wb;
	@JsonProperty
	private String token_qq;
	@JsonProperty
	private String id_wx;
	@JsonProperty
	private String id_wb;
	@JsonProperty
	private String id_qq;

	public int getIs_red() {
		return is_red;
	}
	public void setIs_red(int is_red) {
		this.is_red = is_red;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public char getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(char del_flag) {
		this.del_flag = del_flag;
	}

	public String getToken_wx() {
		return token_wx;
	}
	public void setToken_wx(String token_wx) {
		this.token_wx = token_wx;
	}
	public String getToken_wb() {
		return token_wb;
	}
	public void setToken_wb(String token_wb) {
		this.token_wb = token_wb;
	}
	public String getToken_qq() {
		return token_qq;
	}
	public void setToken_qq(String token_qq) {
		this.token_qq = token_qq;
	}
	public String getId_wx() {
		return id_wx;
	}
	public void setId_wx(String id_wx) {
		this.id_wx = id_wx;
	}
	public String getId_wb() {
		return id_wb;
	}
	public void setId_wb(String id_wb) {
		this.id_wb = id_wb;
	}
	public String getId_qq() {
		return id_qq;
	}
	public void setId_qq(String id_qq) {
		this.id_qq = id_qq;
	}
}
