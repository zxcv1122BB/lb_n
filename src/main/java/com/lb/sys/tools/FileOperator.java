package com.lb.sys.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作类
 *
 * @author lb
 * 2017年12月29日下午5:37:08
 * Copy&前海百仓网
 */
public class FileOperator {

	private static final Logger log = LoggerFactory.getLogger(FileOperator.class);

	/**
	 * 判断文件夹是否存在
	 *
	 * @param folderPath
	 */
	public static void folderIsExist(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			//	开启可写权限
			folder.setWritable(true);
			folder.mkdirs(); // 不存在进行创建
		}
	}

	/**
	 * 文件下载
	 * @param pdfPath
	 * @param pdfName
	 * @param response
	 * @throws IOException
	 */
	public static void downLoadFile(String pdfPath, String pdfName, HttpServletResponse response) throws IOException {

		File f = new File(pdfPath + "/" + pdfName);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset(); // 非常重要
		/*
		 * if (isOnLine) { // 在线打开方式 URL u = new URL("file:///" + filePath);
		 * response.setContentType(u.openConnection().getContentType());
		 * response.setHeader("Content-Disposition", "inline; filename=" +
		 * f.getName()); // 文件名应该编码成UTF-8 } else { // 纯下载方式
		 */
		response.setContentType("application/x-msdownload");
		//下载时必须设置字符编码，不然无法显示中文名
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(),"UTF-8"));
		// }
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
		br.close();
		out.close();
		
		
	}
	
	/**
	 * 下载文件
	 * @param filePathName
	 * @param response
	 * @throws IOException
	 */
	public static void downLoadFile(String filePathName, HttpServletResponse response) throws IOException {

		File f = new File(filePathName);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset(); // 非常重要
		/*
		 * if (isOnLine) { // 在线打开方式 URL u = new URL("file:///" + filePath);
		 * response.setContentType(u.openConnection().getContentType());
		 * response.setHeader("Content-Disposition", "inline; filename=" +
		 * f.getName()); // 文件名应该编码成UTF-8 } else { // 纯下载方式
		 */
		response.setContentType("application/x-msdownload");
		//下载时必须设置字符编码，不然无法显示中文名
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(),"UTF-8"));
		// }
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
		br.close();
		out.close();
		
		log.info("文件:"+filePathName+"下载成功!");
	}

	/**
	 * 删除文件，可以是文件或文件夹
	 *
	 * @param fileName
	 *            要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String pdfPath,String fileName) {
		File file = new File(pdfPath+ "/" + fileName);
		if (!file.exists()) {
			log.info("删除文件失败:" + pdfPath+ "/" +  fileName + "不存在！");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(pdfPath+ "/" +fileName);
			else
				return deleteDirectory(pdfPath+ "/" +fileName);
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName
	 *            要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.info("删除单个文件:" + fileName + "==成功！");
				return true;
			} else {
				log.info("删除单个文件:" + fileName + "==失败！");
				return false;
			}
		} else {
			log.info("删除单个文件失败：" + fileName + "==不存在！");
			return false;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 *
	 * @param dir
	 *            要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileOperator.deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileOperator.deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void renameFile(String pdfPath,String oldFile,String toFile){
		
		String ofile = pdfPath+"/"+oldFile;
		String nfile = pdfPath+"/"+toFile; 
		
        File toBeRenamed = new File(ofile);
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

            return;
        }

        File newFile = new File(nfile);

        //修改文件名
        if (toBeRenamed.renameTo(newFile)) {
        } else {
        }
	}

	/**
	 * 以流形式保存Excel文件
	 *
	 * @param wb
	 * @param filePathName
	 */
	public static void saveExcelFileAsStream(HSSFWorkbook wb, String filePathName) {
		try {
			FileOutputStream fout = new FileOutputStream(filePathName);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createPlist(String url,String version,String title) throws IOException{
        log.info("==========开始创建plist文件");
        //这个地址应该是创建的服务器地址，在这里用生成到本地磁盘地址
        final String path = GetPropertiesValue.getValue("URL","ios_plists_path");
        File file = new File(path);
        if (!file.exists()) {
        	file.setWritable(true);
            file.mkdirs();
        }
        String plistFile = GetPropertiesValue.getValue("URL","plists_name");
        final String PLIST_PATH = path + plistFile;
        file = new File(PLIST_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String sub_title = GetPropertiesValue.getValue("URL","plists_sub_title");
        String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                 + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                 + "<plist version=\"1.0\">\n" + "<dict>\n"
                 + "<key>items</key>\n" 
                 + "<array>\n" 
                 + "<dict>\n"
                 + "<key>assets</key>\n" 
                 + "<array>\n" 
                 + "<dict>\n"
                 + "<key>kind</key>\n"
                 + "<string>software-package</string>\n"
                 + "<key>url</key>\n"
                 //你之前所上传的ipa文件路径
                 + "<string>"+url+"</string>\n" 
                 + "</dict>\n" 
                 + "</array>\n"
                 + "<key>metadata</key>\n"
                 + "<dict>\n"
                 + "<key>bundle-identifier</key>\n"
                 //这个是开发者账号用户名，也可以为空，为空安装时看不到图标，完成之后可以看到
                 + "<string></string>\n"
                 + "<key>bundle-version</key>\n"
                 + "<string>"+version+"</string>\n"
                 + "<key>kind</key>\n"
                 + "<string>software</string>\n"
                 + "<key>subtitle</key>\n"
                 + "<string>"+sub_title+"</string>\n"
                 + "<key>title</key>\n"
                 + "<string>"+title+"</string>\n" 
                 + "</dict>\n" 
                 + "</dict>\n"
                 + "</array>\n"
                 + "</dict>\n"
                 + "</plist>";
        try {
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter writer;
            writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(plist);
            writer.close();
            output.close();
        } catch (Exception e) {
        	log.error("==========创建plist文件异常：" + e.getMessage());
        }
        log.info("==========成功创建plist文件");
        return PLIST_PATH;
    }
}
