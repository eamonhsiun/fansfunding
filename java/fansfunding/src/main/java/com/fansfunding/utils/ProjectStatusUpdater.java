package com.fansfunding.utils;

import com.fansfunding.project.service.ProjectService;

import lombok.extern.log4j.Log4j;

@Log4j
public class ProjectStatusUpdater{
	private ProjectService projectService;
	
	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void updateStatus(){
		log.info("开始更新项目状态");
		int sum=projectService.updateStatus();
		log.info("更新项目状态完成：修改项目"+sum+"个");
	}
}
