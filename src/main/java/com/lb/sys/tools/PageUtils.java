package com.lb.sys.tools;

import java.util.ArrayList;
import java.util.List;

public class PageUtils<T> {

	private Integer pageIndex;//当前页
	
	private Integer pageNum;//每页行数
	
	private Integer pages;//总页数
	
	private Integer rowIndex;//当前行数
	
	private Integer total;//总行数
	
	private List<T> list;
	
	public PageUtils() {
		super();
	}
	
	public PageUtils(Integer pageIndex, Integer pageNum,List<T> list) {
		 if(list!=null && list.size()>0) {
			 this.total = list.size();
			 this.pages = (list.size() % pageNum == 0 ? (list.size() / pageNum) : (list.size() / pageNum) + 1);
			 this.rowIndex = (pageIndex >= 1 ? (pageIndex - 1) * pageNum : 0);// 当前行
			 Integer page = (pageNum > list.size() - rowIndex) ? (list.size() - rowIndex) : pageNum;
			 this.list = list.subList(rowIndex, rowIndex + page);
			 this.pageNum = pageNum;
			 this.pageIndex =  pageIndex; 
		 }else {
			 this.total = 0;
			 this.pages = 0;
			 this.rowIndex = 0;
			 this.list = new ArrayList<T>();
			 this.pageNum = pageNum;
			 this.pageIndex =  pageIndex; 
		 }
	}
	

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
 
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
