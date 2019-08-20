package com.lb.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.report.service.ReportMonthSummaryService;
import com.lb.sys.dao.ReportMonthSummaryMapper;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class ReportMonthSummaryServiceImpl implements ReportMonthSummaryService {
	
	@Autowired
	private ReportMonthSummaryMapper reportMonthSummaryMapper;
		
	@Override
	public List<Map<String, Object>> queryReportMonthSummaryList() {
		return this.reportMonthSummaryMapper.queryReportMonthSummaryList();
	}

}
