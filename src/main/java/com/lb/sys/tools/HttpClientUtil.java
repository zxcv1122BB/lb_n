package com.lb.sys.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.protobuf.TextFormat.ParseException;

public class HttpClientUtil {
	
	/**
	 * 有参的get请求
	 * @param path:请求的路径
	 * @param map:请求的参数
	 * @return
	 */
	public static String doGet(String path, Map<String, String> map) {
		String result = "";
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			// 1,创建客户端httpclient对象
			httpClient = HttpClients.createDefault();
			// 2,定义资源路径
			URIBuilder uriBuilder;
			uriBuilder = new URIBuilder(path);
			if (map != null) {
				for (String key : map.keySet()) {
					uriBuilder.addParameter(key, map.get(key));
				}
			}
			URI uri = uriBuilder.build();
			// 3,创建get对象
			HttpGet get = new HttpGet(uri);
			// 4,执行get请求
			response = httpClient.execute(get);
			// 6,处理结果
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取相应结果
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 无参的get请求
	 * @param path:请求的路径
	 * @return
	 */
	public static String doGet(String path) {
		return doGet(path,null);
	}
	
	/**
	 * post请求,携带参数
	 * @param path:路径
	 * @param map:参数map集合
	 * @return
	 */
	
	@SuppressWarnings("deprecation")
	public static String doPost(String path, Map<String, String> map) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		String result = "";
		try {
			// 1,创建客户端httpclient对象
			httpClient = HttpClients.createDefault();
			// 3,创建post对象
			HttpPost httpPost = new HttpPost(path);
			// 4创建post请求参数
			List<NameValuePair> parameters = new ArrayList<>();
			if (map != null) {
				for (String key : map.keySet()) {
					parameters.add(new BasicNameValuePair(key, map.get(key)));
				}
			}
			// 5模拟表单
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);
			httpPost.setEntity(formEntity);
			// 6,执行get请求
			response = httpClient.execute(httpPost);
			// 7,获取相应结果
			int code = response.getStatusLine().getStatusCode();
			// 8,处理结果
			if (code == 200) {
				// 9,获取相应结果
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 无参的get请求
	 * 
	 * @param path:请求的路径
	 * @return
	 */
	public static String doPost(String path) {
		return doPost(path, null);
	}
	
	public static String doPostJson(String url, String json) {//map json
		//
		String result = "";
		// 1.创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			// 2.创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 3.创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 4.执行http请求
			response = httpClient.execute(httpPost);
			// 5.获取响应结果
			result = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 
	 *@describe:抛出异常,进行捕获处理
	 * @param path
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public static String doGetT(String path, Map<String, String> map) throws Exception {
		String result = "";
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			// 1,创建客户端httpclient对象
			httpClient = HttpClients.createDefault();
			// 2,定义资源路径
			URIBuilder uriBuilder;
			uriBuilder = new URIBuilder(path);
			if (map != null) {
				for (String key : map.keySet()) {
					uriBuilder.addParameter(key, map.get(key));
				}
			}
			URI uri = uriBuilder.build();
			// 3,创建get对象
			HttpGet get = new HttpGet(uri);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
			get.setConfig(requestConfig);
			// 4,执行get请求
			response = httpClient.execute(get);
			// 6,处理结果
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取相应结果
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		}finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * post请求,携带参数
	 * @param path:路径
	 * @param map:参数map集合
	 * @return
	 */
	
	@SuppressWarnings("deprecation")
	public static String doTotalBackStageClient(String path, Map<String, Object> map) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		String result = "";
		try {
			// 1,创建客户端httpclient对象
			httpClient = HttpClients.createDefault();
			// 3,创建post对象
			HttpPost httpPost = new HttpPost(path);
			httpPost.setHeader("Admin-Control", "FORCE");
			// 4创建post请求参数
			List<NameValuePair> parameters = new ArrayList<>();
			if (map != null) {
				for (String key : map.keySet()) {
					parameters.add(new BasicNameValuePair(key, map.get(key).toString()));
				}
			}
			// 5模拟表单
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);
			httpPost.setEntity(formEntity);
			// 6,执行get请求
			response = httpClient.execute(httpPost);
			// 7,获取相应结果
			int code = response.getStatusLine().getStatusCode();
			// 8,处理结果
			if (code == 200) {
				// 9,获取相应结果
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
  
	
}
