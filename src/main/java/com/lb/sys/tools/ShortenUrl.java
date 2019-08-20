/**
 * 
 */
package com.lb.sys.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author wz
 * @describe 创建短连接工具类 通过getSortenURL将长连接传入，导出短链接
 * @date 2017年10月23日
 */
public class ShortenUrl {

	/*@Test
	public void test()
	{
	}*/
	
	private static final String VISIT_URL = "http://api.t.sina.com.cn/short_url/shorten.json";
	
	public static String getSortenURl(String url,String source) {
	        String data = null;  
	  
	        try {  
	            data = postUrl(url,source);  
	        } catch (IOException ex) {  
	       
	        }  
	        if (data != null) {  
	            return changeShortenUrl(data);  
	        } 
	        return null;
	}
	
	private static String changeShortenUrl(String content) {  
        String url = null;  
        JSONArray jsonArray = JSONArray.fromObject(content);
        Object obj = jsonArray.get(0);
        JSONObject fromObject = JSONObject.fromObject(obj);
        url = String.valueOf(fromObject.get("url_short"));
        // url_short
        return url;  
    }
	private static String postUrl(String url,String source)  
            throws IOException {  
        String data = "";  
        if (url != null && source != null) {  
            data += URLEncoder.encode("source", "UTF-8") + "="  
                    + URLEncoder.encode(source, "UTF-8") +"&"
                    +URLEncoder.encode("url_long", "UTF-8") + "="  
                    + URLEncoder.encode(url, "UTF-8");  
        }  
        URL aURL = new java.net.URL(VISIT_URL);  
        HttpURLConnection aConnection = (java.net.HttpURLConnection) aURL  
                .openConnection();  
        try {  
            aConnection.setDoOutput(true);  
            aConnection.setDoInput(true);  
            aConnection.setRequestMethod("POST");  
            // aConnection.setAllowUserInteraction(false);  
            // POST the data  
            OutputStreamWriter streamToAuthorize = new java.io.OutputStreamWriter(  
                    aConnection.getOutputStream());  
            streamToAuthorize.write(data);  
            streamToAuthorize.flush();  
            streamToAuthorize.close();  
  
            // check error  
            int errorCode = aConnection.getResponseCode();  
            if (errorCode >= 400) {  
                InputStream errorStream = aConnection.getErrorStream();  
                try {  
                    String errorData = streamToString(errorStream);  
                    throw new HttpException(errorCode, errorData);  
                } finally {  
                    errorStream.close();  
                }  
            }  
  
            // Get the Response  
            InputStream resultStream = aConnection.getInputStream();  
            try {  
                String responseData = streamToString(resultStream);  
                return responseData;  
            } finally {  
                resultStream.close();  
            }  
        } finally {  
            aConnection.disconnect();  
        }  
    }  
	 private static String streamToString(InputStream resultStream)  
	            throws IOException {  
	        BufferedReader aReader = new java.io.BufferedReader(  
	                new java.io.InputStreamReader(resultStream));  
	        StringBuffer aResponse = new StringBuffer();  
	        String aLine = aReader.readLine();  
	        while (aLine != null) {  
	            aResponse.append(aLine + "\n");  
	            aLine = aReader.readLine();  
	        }  
	        return aResponse.toString();  
	  
	    }  
	 @SuppressWarnings("serial")
	public static class HttpException extends RuntimeException {  
		  
	        private int errorCode;  
	        private String errorData;  
	  
	        public HttpException(int errorCode, String errorData) {  
	            super("HTTP Code " + errorCode + " : " + errorData);  
	            this.errorCode = errorCode;  
	            this.errorData = errorData;  
	        }  
	  
	        public int getErrorCode() {  
	            return errorCode;  
	        }  
	  
	        public String getErrorData() {  
	            return errorData;  
	        }  
	  
	    }  
}
