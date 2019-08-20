package com.lb.sys.config;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.lb.sys.tools.ParseProFileUtil;

@Configuration
public class DruidDBConfig { 
  private Log logger = LogFactory.getLog(DruidDBConfig.class); 
  
  @Value("${spring.datasource.url}") 
  private String dbUrl;
    
  @Value("${spring.datasource.driverClassName}") 
  private String driverClassName; 
    
  @Value("${spring.datasource.initialSize}") 
  private int initialSize; 
    
  @Value("${spring.datasource.minIdle}") 
  private int minIdle; 
    
  @Value("${spring.datasource.maxActive}") 
  private int maxActive; 
    
  @Value("${spring.datasource.maxWait}") 
  private int maxWait; 
  
  @Value("${spring.datasource.timeBetweenEvictionRunsMillis}") 
  private int timeBetweenEvictionRunsMillis; 
    
  @Value("${spring.datasource.minEvictableIdleTimeMillis}") 
  private int minEvictableIdleTimeMillis; 
    
  @Value("${spring.datasource.validationQuery}") 
  private String validationQuery; 
    
  @Value("${spring.datasource.testWhileIdle}") 
  private boolean testWhileIdle; 
    
  @Value("${spring.datasource.testOnBorrow}") 
  private boolean testOnBorrow; 
    
  @Value("${spring.datasource.testOnReturn}") 
  private boolean testOnReturn; 
    
  @Value("${spring.datasource.poolPreparedStatements}") 
  private boolean poolPreparedStatements; 
    
  @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}") 
  private int maxPoolPreparedStatementPerConnectionSize; 
    
  @Value("${spring.datasource.filters}") 
  private String filters; 
    
  @Value("{spring.datasource.connectionProperties}") 
  private String connectionProperties; 
  
  @Bean //声明其为Bean实例 
  @Primary //在同样的DataSource中，首先使用被标注的DataSource 
  public DataSource masterDataSource(){ 
    DruidDataSource datasource = new DruidDataSource(); 
    Map<String, String> map = new HashMap<>();
	String os = System.getProperty("os.name").toLowerCase();
	if (os.contains("windows")) {
		map = ParseProFileUtil.parseProFile("dbData.properties",true);
	} else if (os.contains("linux")) {
		map = ParseProFileUtil.getProperties("/ls/application/conf/dbData.properties",true);
	}
    String dburl = this.dbUrl.replace("@dbIP@", map.get("dbIP"));
    dburl = dburl.replace("@dbPort@", map.get("dbPort"));
    System.err.println(dburl);
    datasource.setUrl(dburl); 
    datasource.setUsername(map.get("dbUser")); 
    datasource.setPassword(map.get("dbPwd")); 
    datasource.setDriverClassName(driverClassName); 
      
    //configuration 
    datasource.setInitialSize(initialSize); 
    datasource.setMinIdle(minIdle); 
    datasource.setMaxActive(maxActive); 
    datasource.setMaxWait(maxWait); 
    datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis); 
    datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis); 
    datasource.setValidationQuery(validationQuery); 
    datasource.setTestWhileIdle(testWhileIdle); 
    datasource.setTestOnBorrow(testOnBorrow); 
    datasource.setTestOnReturn(testOnReturn); 
    datasource.setPoolPreparedStatements(poolPreparedStatements); 
    datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize); 
    try { 
      datasource.setFilters(filters); 
    } catch (SQLException e) { 
      logger.error("druid configuration initialization filter", e); 
    } 
    datasource.setConnectionProperties(connectionProperties); 
      
    return datasource; 
  }
  
  @Bean
  public ServletRegistrationBean druidServlet() {
	  ServletRegistrationBean reg = new ServletRegistrationBean();
	  reg.setServlet(new StatViewServlet());
	  reg.addUrlMappings("/druid/*");
	 /* reg.addInitParameter("loginUsername", "druid");
	  reg.addInitParameter("loginPassword", "jiajian123456");*/
	  return reg;
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
	  FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	  filterRegistrationBean.setFilter(new WebStatFilter());
	  filterRegistrationBean.addUrlPatterns("/*");
	  filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
	  /*filterRegistrationBean.addInitParameter("profileEnable", "true");
	  filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
	  filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");*/
	  return filterRegistrationBean;
  }
} 
