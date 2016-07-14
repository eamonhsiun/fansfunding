package com.fansfunding.utils.pagination;

import com.github.pagehelper.PageInfo;

/**
 * 页面适配器，将PageInfo与数据集适配为Page对象
 * @author w-angler
 *
 */
public class PageAdapter {
	private PageAdapter(){
		throw new IllegalArgumentException("you can not new an intance of this class!");
	}
	
	public static <T> Page adapt(PageInfo<T> info,Object list){
		Page page=new Page();
		page.setFirstPage(info.isIsFirstPage());
		page.setHasNextPage(info.isHasNextPage());
		page.setHasPreviousPage(info.isHasPreviousPage());
		page.setLastPage(info.isIsLastPage());
		page.setPageSize(info.getPageSize());
		page.setPageNum(info.getPageNum());
		page.setPages(info.getPages());
		page.setTotal(info.getTotal());
		page.setList(list);
		return page;
	}
}
