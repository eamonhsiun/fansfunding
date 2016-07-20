package com.fansfunding.user.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.fansfunding.pay.dao.OrderDao;
import com.fansfunding.pay.entity.Order;
import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.user.dao.RealInfoDao;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.RealInfo;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RealInfoDao realInfoDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private FeedbackDao feedbackDao;
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
	
	public User updateUserInfo(int userid,String nickname,String email,Byte sex,
			String idNumber,String intro,Date birthday){
		RealInfo realinfo = realInfoDao.selectByUserId(userid);
		realinfo.setBirthday(birthday);
		realinfo.setSex(sex);
		realinfo.setIdNumber(idNumber);
		realInfoDao.updateByPrimaryKey(realinfo);
		
		User user = userDao.selectById(userid);
		user.setEmail(email);
		user.setNickname(nickname);
		user.setIntro(intro);
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
		userbasic.put("intro", user.getIntro());
		userbasic.put("realInfo", info);
		return userbasic;
	}
	
	public Map<String,Object> getUserBasicMap(User user){
		Map<String,Object> userbasic = new HashMap<>();
		userbasic.put("id", user.getId());
		userbasic.put("nickname", user.getNickname());
		userbasic.put("head", user.getHead());
		userbasic.put("intro", user.getIntro());
		
		return userbasic;
	}
	/**
	 * 搜索用户
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page search(String keyword,int page,int rows){
		List<Map<String,Object>> users=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<User> list=userDao.selectByKeyword(keyword);
		PageInfo<User> info=new PageInfo<>(list);
		list.forEach((user)->{
			users.add(getUserBasicMap(user));
		});
		return PageAdapter.adapt(info, users);
	}
	/**
	 * 获取用户已付款的订单
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */ 
	public Page paidOrder(int userId,int page,int rows){
		List<Map<String,Object>> orders=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<Order> list=orderDao.selectByUserId(userId);
		PageInfo<Order> info=new PageInfo<>(list);
		list.forEach((payOrder)->{
			Map<String,Object> order=new HashMap<>();
			order.put("projectName", projectDao.selectByProjectId(payOrder.getProjectId()));
			order.put("feedbackId",payOrder.getFeedbackId());
			order.put("feedbackTitle", feedbackDao.selectByPrimaryKey(payOrder.getFeedbackId()).getTitle());
			order.put("paidTime", payOrder.getNotifyTime());
			order.put("feedbackDesc", feedbackDao.selectByPrimaryKey(payOrder.getFeedbackId()).getDescription());
			order.put("totalFee",payOrder.getTotalFee());
			order.put("orderStatus",payOrder.getTradeStatus());
		});
		return PageAdapter.adapt(info, orders);
	}
}
