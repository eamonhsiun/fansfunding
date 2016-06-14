package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class User extends UserBasic{
	
	private int is_red;
	
	private String head;
	
	private String email;
	
	private String create_by;
	
	private Date create_time;
	
	private String update_by;
	
	private Date update_time;
	
	private String remark;
	
	private char del_flag;
	
	private String token_wx;
	
	private String token_wb;
	
	private String token_qq;
	
	private String id_wx;
	
	private String id_wb;
	
	private String id_qq;

}
