package com.fansfunding.test.project;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

import com.fansfunding.project.service.ProjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class TestProjectService {
	@Resource
	private ProjectService projectService;
	@Test
	public void testGetByCatagoryId() {
		System.out.println(projectService.getByCatagoryId(1));
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
		System.out.println(projectService.inCatagory(1, 1));
	}

}
