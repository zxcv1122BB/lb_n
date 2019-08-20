package com.lb.sys.controller.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.tools.ResponseUtils;

/**
 * 
 * @author Administrator
 * 数据库备份
 */
@Controller
@RequestMapping("dataBackUpManage")
public class DataBackUpController {

	private static final Logger log = LoggerFactory.getLogger(DataBackUpController.class);

	/**
	 * 数据库备份
	 * @param request
	 */
	@RequestMapping(value = "dataBackUp", method = RequestMethod.GET)
	public ModelAndView getDataBackUp(HttpServletRequest request) {
		try {
			return ResponseUtils.jsonView(200, "数据库备份成功");
		} catch (Exception e) {
			log.error("数据库备份失败。。。异常错误:"+e.getMessage());
		}
		return ResponseUtils.jsonView(201, "数据库备份失败");
	}
	
	
	/** 
     * MySQL数据备份
     * @param hostIP MySQL数据库所在服务器地址IP 
     * @param userName 进入数据库所需要的用户名 
     * @param password 进入数据库所需要的密码 
     * @param savePath 数据库导出文件保存路径 
     * @param fileName 数据库导出文件文件名 
     * @param databaseName 要导出的数据库名 
     * @return 返回true表示导出成功，否则返回false。 
     */  
    public static boolean exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) throws InterruptedException {  
        File saveFile = new File(savePath);  
        if (!saveFile.exists()) {// 如果目录不存在  
            saveFile.mkdirs();// 创建文件夹  
        }  
        if(!savePath.endsWith(File.separator)){  
            savePath = savePath + File.separator;  
        }  
        PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        try {  
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));  
            //windows下
           /* Process process = Runtime.getRuntime().
            		exec("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe -h" 
            + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);  */
            //liunx下
            Process process = Runtime.getRuntime().
            		exec(" mysqldump -h" 
            + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");  
            bufferedReader = new BufferedReader(inputStreamReader);  
            String line;  
            while((line = bufferedReader.readLine())!= null){  
                printWriter.println(line);  
            }  
            printWriter.flush();  
            if(process.waitFor() == 0){//0 表示线程正常终止。  
                return true;  
            }  
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }  
    
    public static void main(String[] args) {
    	try {  
           /* if (exportDatabaseTool("192.168.1.10", "root", "bbt123", "D:/backupDatabase", "ls."+DateUtils.getDateString(new Date(), "yyyyMMddHHmmss")+".sql", "ls")) {  
            } else {  
            }*/  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
}
