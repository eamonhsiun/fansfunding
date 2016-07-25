package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.pay.dao.OrderDao;
import com.fansfunding.pay.entity.Order;
import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.dao.FollowProjectDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.dao.ProjectDetailDao;
import com.fansfunding.project.dao.ProjectMomentDao;
import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.FollowProject;
import com.fansfunding.project.entity.Project;
import com.fansfunding.project.entity.ProjectDetail;
import com.fansfunding.project.entity.ProjectMoment;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ProjectService {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectDetailDao detailDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private FeedbackDao feedbackDao;
	@Autowired
	private ProjectMomentDao momentDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private FollowProjectDao followProjectDao;
	@Autowired
	private UserService userService;
	/**
	 * 添加项目
	 * @param name 
	 * @param categoryId 
	 * @param cover 
	 * @param sponsor 
	 * @param targetDeadline 
	 * @param targetMoney 
	 * @param description 
	 * @param content 
	 * @param others 
	 * @param video 
	 * 
	 */
	public int addProject(String name, Integer categoryId, String cover, Integer sponsor, 
			Date targetDeadline, Long targetMoney, String description, String content,String images,
			String others, String video){
		ProjectDetail projectDetail = new ProjectDetail();
		projectDetail.setContent(content);
		projectDetail.setCreateBy(userDao.selectById(sponsor).getName());
		projectDetail.setUpdateBy(userDao.selectById(sponsor).getName());
		projectDetail.setDelFlag("0");
		projectDetail.setOthers(others);
		projectDetail.setVideo(video);
		detailDao.insert(projectDetail);

		Project project = new Project();
		project.setCategoryId(categoryId);
		project.setCover(cover);
		project.setCreateBy(userDao.selectById(sponsor).getName());
		project.setDelFlag("0");
		project.setDescription(description);
		project.setUpdateBy(userDao.selectById(sponsor).getName());
		project.setDetailId(projectDetail.getId());
		project.setName(name);
		project.setSponsor(sponsor);
		project.setStatus("2");
		project.setTargetDeadline(targetDeadline);
		project.setTargetMoney(targetMoney);
		projectDao.insert(project);
		Resource res=new Resource();
		res.setMappingId(project.getId());
		res.setType("moment_image");
		res.setPath(cover);
		resourceDao.updateByPath(res);
		for(String s:images.split(",")){
			Resource resource=new Resource();
			resource.setMappingId(project.getId());
			resource.setType("project_image");
			resource.setPath(s);
			resourceDao.updateByPath(resource);
		}
		return project.getId();
	}

	/**
	 * 获取分类下所有项目
	 * @param categroyId 分类ID
	 * @return
	 */
	public Page getByCategoryId(int categoryId,int page,int rows){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();

		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectByCategoryId(categoryId);
		PageInfo<Project> info=new PageInfo<>(list);

		list.forEach((e)->{
			projects.add(this.buildMap(e));
		});

		return PageAdapter.adapt(info, projects);
	}

	/**
	 * 根据项目ID获取项目详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getByProjectId(Integer projectId){
		return this.buildMap(projectDao.selectByProjectId(projectId));
	}
	private Map<String,Object> buildMap(Project prj){
		Map<String,Object> project=new HashMap<>();
		project.put("id", prj.getId());
		project.put("categoryId", prj.getCategoryId());
		project.put("cover", prj.getCover());
		project.put("description", prj.getDescription());
		project.put("detailId", prj.getDetailId());
		project.put("name", prj.getName());
		project.put("sponsor", prj.getSponsor());
		project.put("status", prj.getStatus());
		project.put("targetDeadline", prj.getTargetDeadline());
		project.put("targetMoney", prj.getTargetMoney());
		project.put("sponsorNickname", userDao.selectById(prj.getSponsor()).getNickname());
		project.put("sponsorHead", userDao.selectById(prj.getSponsor()).getHead());
		project.put("createTime",prj.getCreateTime());
		project.putAll(this.support(prj.getId()));
		return project;
	}
	/**
	 * 获取详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getDetails(Integer projectId){
		Map<String,Object> details=new HashMap<String,Object>();
		ProjectDetail pd=detailDao.selectByProjectId(projectId);

		details.put("id", pd.getId());
		details.put("content", pd.getContent());
		List<Resource> images=resourceDao.selectProjectImages(projectId);
		String[] paths=new String[images.size()];
		for(int i=0;i<images.size();i++){
			paths[i]=images.get(i).getPath();
		}
		details.put("images", paths);
		details.put("video", pd.getVideo());
		details.put("others", pd.getOthers());

		return details;
	}
	/**
	 * 判断该项目是否在该分类目录下
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	public boolean  inCategory(int categoryId,int projectId){
		Project project=projectDao.selectByProjectId(projectId);
		if(project==null){
			return false;
		}
		return project.getCategoryId().intValue()==categoryId;
	}


	/**
	 * 获取项目动态
	 * @param projectId 项目id
	 * @param page 
	 * @param rows
	 * @return
	 */
	public Page moment(int projectId,int page,int rows){
		List<Map<String,Object>> moments=new ArrayList<Map<String,Object>>();

		PageHelper.startPage(page, rows);
		List<ProjectMoment> list=momentDao.selectByProjectId(projectId);
		PageInfo<ProjectMoment> info=new PageInfo<>(list);

		list.forEach((e)->{
			Map<String,Object> moment=new HashMap<>();
			moment.put("sponsor", projectDao.selectByProjectId(projectId).getSponsor());
			moment.put("sponsorNickname", 
					userDao.selectById(projectDao.selectByProjectId(projectId).getSponsor()).getNickname());
			moment.put("sponsorHead", 
					userDao.selectById(projectDao.selectByProjectId(projectId).getSponsor()).getHead());
			List<Resource> images=resourceDao.selectMomentImages(projectId);
			String[] paths=new String[images.size()];
			for(int i=0;i<images.size();i++){
				paths[i]=images.get(i).getPath();
			}
			moment.put("images", paths);
			moment.put("content", e.getContent());
			moment.put("updateTime", e.getCreateTime());
			moments.add(moment);
		});
		return PageAdapter.adapt(info, moments);
	}
	/**
	 * 添加动态
	 * @param category
	 * @param projectId
	 * @param moment
	 * @param images
	 */
	public boolean addMoment(int category,int projectId,ProjectMoment moment,String images,int sponsorId){
		if(sponsorId!=projectDao.selectByProjectId(projectId).getSponsor().intValue()){
			return false;
		}
		momentDao.insert(moment);
		for(String s:images.split(",")){
			Resource resource=new Resource();
			resource.setMappingId(moment.getId());
			resource.setType("moment_image");
			resource.setPath(s);
			resourceDao.updateByPath(resource);
		}
		return true;
	}
	/**
	 * 获取项目的已支持人数和已支持金额
	 * @param projectId
	 * @return
	 */
	public Map<String,Object> support(int projectId){
		List<Order> orders=orderDao.selectProjectSum(projectId);
		double sum=0.0;
		for(Order order:orders){
			sum+=Double.parseDouble(order.getTotalFee());
		}
		Map<String,Object> supports=new HashMap<>();
		supports.put("sum",sum);
		supports.put("supportNum",orders.size());
		return supports;
	}
	/**
	 * 
	 * 搜索项目
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page search(String keyword,int page,int rows){

		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();

		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectByKeyword(keyword);
		PageInfo<Project> info=new PageInfo<>(list);
		list.forEach((e)->{
			projects.add(this.buildMap(e));
		});
		return PageAdapter.adapt(info, projects);
	}
	/**
	 * 获取用户发起的项目
	 * @param userId 用户id
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page getSponsor(int userId,int page,int rows){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectSponsor(userId);
		PageInfo<Project> info=new PageInfo<>(list);
		list.forEach((e)->{
			projects.add(this.buildMap(e));
		});
		return PageAdapter.adapt(info, projects);
	}
	/**
	 * 获取关注的项目
	 * @param userId 用户id
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page getFollow(int userId,int page,int rows){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectFollow(userId);
		PageInfo<Project> info=new PageInfo<>(list);
		list.forEach((e)->{
			projects.add(this.buildMap(e));
		});
		return PageAdapter.adapt(info, projects);
	}
	/**
	 * 获取关注的项目
	 * @param userId 用户id
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page getSupport(int userId,int page,int rows){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectSupport(userId);
		PageInfo<Project> info=new PageInfo<>(list);
		list.forEach((e)->{
			projects.add(this.buildMap(e));
		});
		return PageAdapter.adapt(info, projects);
	}
	/**
	 * 关注
	 * @param userId
	 * @param category
	 * @param projectId
	 */
	public boolean follow(int userId,int category,int projectId){
		if(userDao.selectById(userId)==null){
			return false;
		}
		FollowProject follow=new FollowProject();
		follow.setProjectId(projectId);
		follow.setUserId(userId);
		if(followProjectDao.select(follow)!=null){
			followProjectDao.disdelete(follow);
		}
		else{
			followProjectDao.insert(follow);
		}
		return true;
	}
	/**
	 * 取消关注
	 * @param userId
	 * @param category
	 * @param projectId
	 */
	public boolean unfollow(int userId,int category,int projectId){
		if(userDao.selectById(userId)==null){
			return false;
		}
		FollowProject follow=new FollowProject();
		follow.setProjectId(projectId);
		follow.setUserId(userId);
		followProjectDao.delete(follow);
		return true;
	}
	/**
	 * 判断某个用户是否关注了某个项目
	 * @param userId 用户id 
	 * @param projectId 项目id
	 * @return
	 */
	public boolean isFollower(int userId,int projectId){
		FollowProject follow=new FollowProject();
		follow.setProjectId(projectId);
		follow.setUserId(userId);
		FollowProject fp=followProjectDao.select(follow);
		if(fp==null||fp.getDelFlag().equals("1")){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public Page getFollowers(int projectId,int page,int rows){
		List<Map<String,Object>> followers=new ArrayList<Map<String,Object>>();
		PageHelper.startPage(page, rows);
		List<User> list=userDao.selectFollowers(projectId);
		PageInfo<User> info=new PageInfo<>(list);
		list.forEach((user)->{
			followers.add(userService.getUserBasicMap(user));
		});
		return PageAdapter.adapt(info, followers);
	}
}
