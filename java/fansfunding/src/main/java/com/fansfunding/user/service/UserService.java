package com.fansfunding.user.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.RealInfoDao;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.RealInfo;
import com.fansfunding.user.entity.User;


@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RealInfoDao realInfoDao;
	/**
	 * @param id
	 * @param name
	 * @return
	 */
	public User getUser(String id,String name){
		if(!id.equals(""))
			return getUserById(Integer.parseInt(id));
		else 
			return getUserByName(name);
	}
	
	
	/**
	 * @param uid
	 * @return
	 */
	public User getUserById(int uid){
		User user = userDao.selectById(uid);
		user.setRealInfo(realInfoDao.selectByUserId(uid));
		return user;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public User getUserByName(String name){
		return userDao.selectByName(name);
	}
	

	/**
	 * @param phone
	 * @return
	 */
	public User getUserByPhone(String phone){
		return userDao.selectByPhone(phone);
	}
	
	/**
	 * @param pwd1
	 * @param token
	 * @param pwd2
	 * @return
	 */
	public boolean CheckPwd(String pwd1,String pwd2){
		return pwd1.equals(pwd2);

	}
	

	
	/**
	 * @param phone
	 * @param password
	 * @return
	 */
	public User createUser(String phone,String password,int tokenid){
		User user = new User();	
		user.setName(phone);
		user.setNickname(phone);
		user.setPassword(password);
		user.setPhone(phone);
		user.setIs_red(0);
		user.setHead(UUID.randomUUID().toString().replace("-", ""));
		user.setRemark("");
		user.setDel_flag('0');
		user.setToken(tokenid);
		user.setCreate_by("me");
		user.setUpdate_by("me");
		user.setCreate_time(new Date());
		user.setUpdate_time(new Date());
		userDao.insertNewUser(user);
		
		RealInfo realInfo = new RealInfo();
		realInfo.setUserId(user.getId());
//		realInfo.setSex((byte) 0);
//		realInfo.setAddress("");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
//		try {
//			realInfo.setBirthday(sdf.parse("1999-12-31"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
//		realInfo.setBirthPlace("");
//		realInfo.setCreateBy("me");
//		realInfo.setCreateTime(new Date());
//		realInfo.setUpdateBy("me");
//		realInfo.setUpdateTime(new Date());
//		realInfo.setDelFlag("0");
//		realInfo.setIdNumber("");
//		realInfo.setIsValidated((byte)0);
//		realInfo.setRealName("");
//		realInfo.setSex((byte)0);
		
		realInfoDao.insertNew(realInfo);
		
		user.setRealInfo(realInfo);
		return user;
	}
		


	public void updateToken(int id) {
		
	}


	public void updatePwd(User user) {
		userDao.updatePwd(user);
	}
	
	public void updateNickName(int userid,String nickname){
		User user =userDao.selectById(userid);
		user.setNickname(nickname);
		userDao.updateNickName(user);
	}
	
	public User updateUserInfo(int userid,String email,Byte sex,String idNumber,Date birthday){
		RealInfo realinfo = realInfoDao.selectByUserId(userid);
		realinfo.setBirthday(birthday);
		realinfo.setSex(sex);
		realinfo.setIdNumber(idNumber);
		realInfoDao.updateByPrimaryKey(realinfo);
		
		User user = userDao.selectById(userid);
		user.setEmail(email);
		userDao.updateUser(user);
		user.setRealInfo(realinfo);
		return user;
	}
	
	
	
	

	public Set<String> findRoles(String username){
		return null;
	}
	

	public Set<String> findPermissions(String username) {
		return null;
	}
	
	public Map<String,Object> getUserMap(User user){
		RealInfo realInfo = user.getRealInfo();
		Map<String,Object> info = new HashMap<>();
		if(realInfo!=null){
			info.put("realName", realInfo.getRealName());
			info.put("sex", realInfo.getSex());
			info.put("birthPlacr", realInfo.getBirthPlace());
			info.put("birthday", realInfo.getBirthday());
		}
		
		Map<String,Object> userbasic = new HashMap<>();
		userbasic.put("id", user.getId());
		userbasic.put("name", user.getName());
		userbasic.put("nickname", user.getNickname());
		userbasic.put("phone",user.getPhone());
		userbasic.put("is_red", user.getIs_red());
		userbasic.put("head", user.getHead());
		userbasic.put("email", user.getEmail());
		userbasic.put("realInfo", info);
		return userbasic;
	}
	
	public Map<String,Object> getUserBasicMap(User user){
		Map<String,Object> userbasic = new HashMap<>();
		userbasic.put("id", user.getId());
		userbasic.put("nickname", user.getNickname());
		userbasic.put("head", user.getHead());
		
		return userbasic;
	}
	
}
