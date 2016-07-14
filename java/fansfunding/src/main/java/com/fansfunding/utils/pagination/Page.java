package com.fansfunding.utils.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面
 * @author w-angler
 *
 */
@Data
@NoArgsConstructor
public class Page {
	/**
	 * 对象记录结果集
	 */
	private Object list; 
	/**
	 * 总记录数
	 */
	private long total; 
	/**
	 * 每页显示记录数
	 */
	private int pageSize; 
	/**
	 * 总页数
	 */
	private int pages; 
	/**
	 * 当前页
	 */
	private int pageNum; 

	/**
	 * 是否为第一页
	 */
	private boolean isFirstPage; 
	/**
	 * 是否为最后一页
	 */
	private boolean isLastPage; 
	/**
	 * 是否有前一页
	 */
	private boolean hasPreviousPage;
	/**
	 * 是否有下一页
	 */
	private boolean hasNextPage;
	
}
