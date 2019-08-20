package com.lb.open.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lsopen.open.DataTools;

@Component
public class OpenJob {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Scheduled(fixedRate = 24*60*60*1000)  
	public void a () {
		DataTools.setJdbcTemplate(jdbcTemplate);
		jdbcTemplate.execute("delete from open_task_log  where  datediff(curdate(), create_time)>10 and open_state=1");
	}
}
