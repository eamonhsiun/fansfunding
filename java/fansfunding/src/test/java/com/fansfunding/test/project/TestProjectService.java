package com.fansfunding.test.project;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.entity.Project;
import com.fansfunding.project.service.ProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class TestProjectService {
	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectDao projectDao;
	@Test
	public void testGetByCatagoryId() {
		System.out.println(projectService.getByCategoryId(1,1,1));
	}

	@Test
	public void testGetByProjectId() {
		System.out.println(projectService.getByProjectId(1));
	}

	@Test
	public void testGetDetails() {
		System.out.println(projectService.getDetails(1));
	}

	@Test
	public void testInCatagory() {
		System.out.println(projectService.inCategory(1, 1));
	}
	@Test
	public void testPagination(){
		PageHelper.startPage(3, 2);
		List<Project> list=projectDao.selectByCategoryId(1);
		list.forEach((e)->{
			System.out.println(e);
		});
		PageInfo<Project> info=new PageInfo<Project>(list);
		System.out.println(info);
	}

}
