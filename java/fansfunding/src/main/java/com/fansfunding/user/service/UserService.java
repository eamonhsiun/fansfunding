package com.fansfunding.user.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.pay.dao.OrderDao;
import com.fansfunding.pay.entity.Order;
import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Feedback;
import com.fansfunding.project.entity.Project;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.user.dao.RealInfoDao;
import com.fansfunding.user.dao.ShoppingAddressDao;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.dao.UserMomentDao;
import com.fansfunding.user.entity.RealInfo;
import com.fansfunding.user.entity.ShoppingAddress;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.fileupload.DefaultImage;
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
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ShoppingAddressDao addressDao;
	@Autowired
	private UserMomentDao userMomentDao;
	/**
	 * 根据用户名或者id获得用户
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

	public boolean isEmailExist(String email){
		return userDao.selectByEmail(email)!=null;
	}
	/**
	 * 根据id获取用户
	 * @param uid
	 * @return
	 */
	public User getUserById(int uid){
		User user = userDao.selectById(uid);
		user.setRealInfo(realInfoDao.selectByUserId(uid));
		return user;
	}
	/**
	 * 判断用户是否存在
	 * @param userId
	 * @return
	 */
	public boolean isExist(int userId){
		return userDao.selectById(userId)!=null;
	}
	/**
	 * 根据用户名获取用户
	 * @param name
	 * @return
	 */
	public User getUserByName(String name){
		return userDao.selectByName(name);
	}


	/**
	 * 根据手机号获取用户
	 * @param phone
	 * @return
	 */
	public User getUserByPhone(String phone){
		return userDao.selectByPhone(phone);
	}

	/**
	 * 检查密码是否一致
	 * @param pwd1
	 * @param token
	 * @param pwd2
	 * @return
	 */
	public boolean CheckPwd(String pwd1,String pwd2){
		return pwd1.equals(pwd2);

	}

	/**
	 * 创建用户
	 * @param phone
	 * @param password
	 * @return
	 */
	public User createUser(String phone,String password){
		User user = new User();	
		user.setName(phone);
		user.setNickname(phone);
		user.setPassword(password);
		user.setPhone(phone);
		user.setIs_red(0);
		user.setHead(DefaultImage.DEFAULT_HEAD);
		user.setRemark("");
		user.setDel_flag('0');
		user.setCreate_by("admin");
		user.setUpdate_by("admin");
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

	/**
	 * 更新密码
	 * @param user
	 */
	public void updatePwd(User user) {
		userDao.updatePwd(user);
	}
	/**
	 * 更新昵称
	 * @param userid
	 * @param nickname
	 */
	public void updateNickName(int userid,String nickname){
		User user =userDao.selectById(userid);
		user.setNickname(nickname);
		userDao.updateNickName(user);
	}
	/**
	 * 更新用户信息
	 * @param userid 用户id
	 * @param nickname 昵称
	 * @param email 邮箱
	 * @param sex 性别
	 * @param idNumber 身份证
	 * @param intro 个人介绍
	 * @param birthday 生日
	 * @return
	 */
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
	/**
	 * 获取用户Map
	 * @param user
	 * @return
	 */
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
		userbasic.put("followerNum", userDao.selectFollowers(user.getId()).size());
		userbasic.put("followingNum", userDao.selectFollowing(user.getId()).size());
		userbasic.put("momentNum", userMomentDao.selectByUserId(user.getId()).size());
		return userbasic;
	}
	/**
	 * 获取用户基本信息（可见的）
	 * @param user
	 * @return
	 */
	public Map<String,Object> getUserBasicMap(User user){
		if(user==null)return null;
		Map<String,Object> userbasic = new HashMap<>();
		userbasic.put("id", user.getId());
		userbasic.put("nickname", user.getNickname());
		userbasic.put("head", user.getHead());
		userbasic.put("intro", user.getIntro());
		return userbasic;
	}
	public Map<String,Object> getUserBasicMap(int userId){
		return this.getUserBasicMap(userDao.selectById(userId));
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

			Project prj=projectDao.selectByProjectId(payOrder.getProjectId());
			order.put("projectId", prj.getId());
			order.put("projectName", prj.getName());
			order.put("categoryId", prj.getCategoryId());

			Feedback feedback=feedbackDao.selectByPrimaryKey(payOrder.getFeedbackId());

			order.put("feedbackId",payOrder.getFeedbackId());
			order.put("feedbackTitle", feedback.getTitle());
			order.put("feedbackDesc", feedback.getDescription());
			List<Resource> images=resourceDao.selectFeedbackImages(feedback.getId());
			String[] paths=new String[images.size()];
			for(int i=0;i<images.size();i++){
				paths[i]=images.get(i).getPath();
			}
			order.put("feedbackImages", paths);

			order.put("paidTime", payOrder.getNotifyTime());
			order.put("totalFee",payOrder.getTotalFee());
			order.put("orderStatus",orderStatus(payOrder.getTradeStatus()));
			order.put("orderNo",payOrder.getOrderNo());
			order.put("tradeNo",payOrder.getTradeNo());
			orders.add(order);
		});
		return PageAdapter.adapt(info, orders);
	}
	/**
	 * 根据订单号获取订单信息
	 * @param orderNo 订单号
	 * @return
	 */
	public Map<String,Object> paidOrder(String orderNo){
		Map<String,Object> orderInfo=new HashMap<>();
		Order order=orderDao.selectByOrderNo(orderNo);
		
		Project prj=projectDao.selectByProjectId(order.getProjectId());
		orderInfo.put("projectId", prj.getId());
		orderInfo.put("projectName", prj.getName());
		orderInfo.put("categoryId", prj.getCategoryId());

		Feedback feedback=feedbackDao.selectByPrimaryKey(order.getFeedbackId());
		orderInfo.put("feedbackId",order.getFeedbackId());
		orderInfo.put("feedbackTitle", feedback.getTitle());
		orderInfo.put("feedbackDesc", feedback.getDescription());
		List<Resource> images=resourceDao.selectFeedbackImages(feedback.getId());
		String[] paths=new String[images.size()];
		for(int i=0;i<images.size();i++){
			paths[i]=images.get(i).getPath();
		}
		orderInfo.put("feedbackImages", paths);

		orderInfo.put("paidTime", order.getNotifyTime());
		orderInfo.put("totalFee",order.getTotalFee());
		orderInfo.put("orderStatus",orderStatus(order.getTradeStatus()));
		orderInfo.put("orderNo",order.getOrderNo());
		orderInfo.put("tradeNo",order.getTradeNo());
		
		ShoppingAddress address=addressDao.selectByPrimaryKey(order.getAddressId());
		Map<String,Object> shoppingAddress=new HashMap<>();
		shoppingAddress.put("province",address.getProvince());
		shoppingAddress.put("city",address.getCity());
		shoppingAddress.put("district",address.getDistrict());
		shoppingAddress.put("address",address.getAddress());
		shoppingAddress.put("phone",address.getPhone());
		shoppingAddress.put("name", address.getName());
		shoppingAddress.put("postCode",address.getPostCode());
		orderInfo.put("address", shoppingAddress);
		
		return orderInfo;
	}
	/**
	 * 转换支付状态字符描述
	 * @param rawStatus
	 * @return
	 */
	private String orderStatus(String rawStatus){
		if("TRADE_SUCCESS".equals(rawStatus)){
			return "支付成功";
		}
		if("TRADE_FINISHED".equals(rawStatus)){
			return "支付完成";
		}
		return "支付失败";
	}
}
