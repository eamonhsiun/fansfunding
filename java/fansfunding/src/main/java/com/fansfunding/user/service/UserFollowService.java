package com.fansfunding.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.FollowUserDao;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.FollowUser;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserFollowService {
	@Autowired
	private FollowUserDao followUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	/**
	 * 关注
	 * @param userId
	 * @param category
	 * @param projectId
	 */
	public boolean follow(int userId,int followerId){
		if(CheckUtils.isNullOrEmpty(userDao.selectById(userId),userDao.selectById(followerId))){
			return false;
		}
		FollowUser follow=new FollowUser();
		follow.setUserId(userId);
		follow.setFollowerId(followerId);
		if(followUserDao.select(follow)!=null){
			followUserDao.disdelete(follow);
		}
		else{
			followUserDao.insert(follow);
		}
		return true;
	}
	/**
	 * 取消关注
	 * @param userId
	 * @param category
	 * @param projectId
	 */
	public boolean unfollow(int userId,int followerId){
		if(CheckUtils.isNullOrEmpty(userDao.selectById(userId),userDao.selectById(followerId))){
			return false;
		}
		FollowUser follow=new FollowUser();
		follow.setUserId(userId);
		follow.setFollowerId(followerId);
		followUserDao.delete(follow);
		return true;
	}
	/**
	 * 获取用户关注的用户
	 * @param userId 用户id
	 * @return
	 */
	public Page getFollowing(int userId,int page,int rows){
		List<Map<String,Object>> following=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<User> list=userDao.selectFollowing(userId);
		PageInfo<User> info=new PageInfo<>(list);
		list.forEach((user)->{
			following.add(userService.getUserBasicMap(user));
		});
		return PageAdapter.adapt(info, following);
	}
	/**
	 * 获取用户的关注者
	 * @param userId 用户id
	 * @return
	 */
	public Page getFollowers(int userId,int page,int rows){
		List<Map<String,Object>> followers=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<User> list=userDao.selectFollowers(userId);
		PageInfo<User> info=new PageInfo<>(list);
		list.forEach((user)->{
			followers.add(userService.getUserBasicMap(user));
		}); 
		return PageAdapter.adapt(info, followers);
	}
}
