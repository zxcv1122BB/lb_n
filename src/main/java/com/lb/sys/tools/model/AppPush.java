package com.lb.sys.tools.model;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;

public class AppPush {

	//一定要弹出来啊
	private static String appId = "iAQ6wGElV9AwCVsc2IjBY1";//应用id
	private static String appKey = "Q319BboQeNAn5iltT4W4j5";//
	private static String masterSecret = "xVhn0081SwAR79uxaN6Wk4";
	private static String url = "http://sdk.open.api.igexin.com/apiex.htm";// 向所有app下的用户推送
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";// 向CID推送
	static String CID1 = "a212286963d9cb76a08f274d8c07995e";
	// static String CID1 = "";
	static String CID2 = "";

	// 向app所有用户推消息   //不带url
	@SuppressWarnings("deprecation")
	public static String sendAll(String title, String text) {
		// 对app下面所有的用户推送
		IGtPush push = new IGtPush(url, appKey, masterSecret);
		// 定义"点击链接打开通知模板"，并设置标题、内容、链接
		//LinkTemplate template = new LinkTemplate();
		NotificationTemplate template=new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTitle(title);
		template.setText(text);
//		if(urlStr !=null && !(urlStr.equals(""))) {
//			template.setUrl(urlStr);
//		} 
		List<String> appIds = new ArrayList<String>();
		appIds.add(appId);
		// 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
		AppMessage message = new AppMessage();
		message.setData(template);
		message.setAppIdList(appIds);
		message.setOffline(true);
		message.setOfflineExpireTime(1000 * 600);
//		if(startTime!=null && endTime!=null) {
//			try {
//				template.setDuration(startTime, endTime);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		IPushResult ret = push.pushMessageToApp(message);
		return ret.getResponse().toString();
	}

	/****
	 * 给app用户绑定别名
	 * 
	 * @param Alias
	 *            所取别名
	 * @param CID
	 *            app别名
	 * @return 绑定结果
	 */
	@SuppressWarnings("unused")
	public static String updateCIDByAlias(String Alias, String CID) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IAliasResult bindSCid = push.bindAlias(appId, Alias, CID);
		return null;
	}

	/***
	 * 查询某一个用户是否在线
	 */
	@SuppressWarnings("unused")
	public static String getUserStatus(String CID) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IQueryResult abc = push.getClientIdStatus(appId, CID);
		return null;
	}

	/***
	 * 别名或者cid推送
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String aliasOrCidSend(List listCID, List listBIE, String title, String context) {
		// 配置返回每个用户返回用户状态，可选
		System.setProperty("gexin_pushList_needDetails", "true");
		// 配置返回每个别名及其对应cid的用户状态，可选
		// System.setProperty("gexin_pushList_needAliasDetails", "true");
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		// 通知透传模板
		NotificationTemplate template = notificationTemplateDemo(title, context);
		ListMessage message = new ListMessage();
		message.setData(template);
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		IPushResult ret = null;
		// taskId用于在推送时去查找对应的message
		String taskId = push.getContentId(message);
		if (listCID != null) {
			ret = push.pushMessageToList(taskId, listCID);
		} else {
			ret = push.pushMessageToList(taskId, listBIE);
		}
		return ret.getResponse().toString();
	}

	/**
	 * 根据别名获取CID
	 * 
	 * @param bie
	 *            别名
	 * @return 别名集合，因为一个cid不只对应一个别名
	 */
	public static List<String> getCid(String bie) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IAliasResult queryClient = push.queryClientId(appId, bie);
		return queryClient.getClientIdList();
	}

	/***
	 * 通过CID获取别名
	 * 
	 * @param CID
	 * @return
	 */
	public static String getAlias(String CID) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IAliasResult queryAlias = push.queryAlias(appId, CID);
		return queryAlias.getAlias();
	}

	/***
	 * 解除绑定
	 * 
	 * @param bie
	 *            别名
	 * @param cid
	 *            cid
	 * @return 成功或者失败
	 * @throws Exception
	 *             异常
	 */
	public static boolean AliasUnBind(String bie, String cid) throws Exception {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IAliasResult AliasUnBind = push.unBindAlias(appId, bie, cid);
		return AliasUnBind.getResult();
	}

	/**
	 * 设置标签标签内容
	 * @param tagList
	 * @return
	 */
	public static String setTag(List<String> tagList,String cid) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		IQueryResult ret = push.setClientTag(appId, cid, tagList);
		return ret.getResponse().toString();
	}
	
	
    public static String sendAlias(String title,String context,String Cid) {
    	  IGtPush push = new IGtPush(host, appKey, masterSecret);
	        NotificationTemplate template = notificationTemplateDemo(title,context);
	        SingleMessage message = new SingleMessage();
	        message.setOffline(true);
	        // 离线有效时间，单位为毫秒，可选
	        message.setOfflineExpireTime(24 * 3600 * 1000);
	        message.setData(template);
	        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
	        message.setPushNetWorkType(0);
	        Target target = new Target();
	        target.setAppId(appId);
	        target.setClientId(Cid);
	        //target.setAlias(Alias);
	        IPushResult ret = null;
	        try {
	            ret = push.pushMessageToSingle(message, target);
	        } catch (RequestException e) {
	            e.printStackTrace();
	            ret = push.pushMessageToSingle(message, target, e.getRequestId());
	        }
	        return ret.getResponse().get("result").toString();
    }
	
	
	
//	 public static void main(String[] args) throws Exception {
//	        IGtPush push = new IGtPush(host, appKey, masterSecret);
//	        NotificationTemplate template = notificationTemplateDemo("", "");
//	        SingleMessage message = new SingleMessage();
//	        message.setOffline(true);
//	        // 离线有效时间，单位为毫秒，可选
//	        message.setOfflineExpireTime(24 * 3600 * 1000);
//	        message.setData(template);
//	        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//	        message.setPushNetWorkType(0);
//	        Target target = new Target();
//	        target.setAppId(appId);
//	        target.setClientId(CID1);
//	        //target.setAlias(Alias);
//	        IPushResult ret = null;
//	        try {
//	            ret = push.pushMessageToSingle(message, target);
//	        } catch (RequestException e) {
//	            e.printStackTrace();
//	            ret = push.pushMessageToSingle(message, target, e.getRequestId());
//	        }
//	        if (ret != null) {
//	        } else {
//	        }
//	    }
//	    public static LinkTemplate linkTemplateDemo() {
//	        LinkTemplate template = new LinkTemplate();
//	        // 设置APPID与APPKEY
//	        template.setAppId(appId);
//	        template.setAppkey(appKey);
//
//	        Style0 style = new Style0();
//	        // 设置通知栏标题与内容
//	        style.setTitle("请输入通知栏标题");
//	        style.setText("请输入通知栏内容");
//	        // 配置通知栏图标
//	        style.setLogo("icon.png");
//	        // 配置通知栏网络图标
//	        style.setLogoUrl("");
//	        // 设置通知是否响铃，震动，或者可清除
//	        style.setRing(true);
//	        style.setVibrate(true);
//	        style.setClearable(true);
//	        template.setStyle(style);
//
//	        // 设置打开的网址地址
//	        template.setUrl("http://www.baidu.com");
//	        return template;
//	    }
	
	
	

	public static void main(String[] args) throws Exception {
		 List<String> tagList = new ArrayList<String>();
         tagList.add("0");
		
		
		 //别名推送 普通推送
//		 List targets = new ArrayList();
//		 Target target1 = new Target();
//		 Target target2 = new Target();
//		 target1.setAppId(appId);
//		 target1.setAlias("tjr");
//		 target2.setAppId(appId);
//		 target2.setAlias("ly");
//		 targets.add(target1);
//		 targets.add(target2);

//		 //群推消息，打开Url IIGtPush push = new IGtPush(host, appKey, masterSecret); IBatch
//		IIGtPush push = new IGtPush(host, appKey, masterSecret);
//        IBatch batch = push.getBatch();

//       try {
//           //构建客户a的透传消息a
//        	constructClientLinkMsg(CID1,"消息1",batch,"http://www.baidu");
//            //构建客户B的点击通知打开网页消息b
//        	constructClientLinkMsg(CID2,"消息2",batch,"http://www.baidu");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        IPushResult submit = batch.submit();
        
		getUserStatus(CID1);
	}

	// --------------------------工具实体类-------------------------------
	public static NotificationTemplate notificationTemplateDemo(String title, String context) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        //template.setTransmissionContent("请输入您要透传的内容");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(context);
        // 配置通知栏图标
        //style.setLogo("icon.png");
        // 配置通知栏网络图标
        //style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        return template;
    }
	
	
	
	
//	public static NotificationTemplate notificationTemplateDemo(String title, String context) {
//		NotificationTemplate template = new NotificationTemplate();
//		// 设置APPID与APPKEY
//		template.setAppId(appId);
//		template.setAppkey(appKey);
//		Style0 style = new Style0();
//		// 设置通知栏标题与内容
//		style.setTitle(title);
//		style.setText(context);
//		// // 配置通知栏图标
//		// style.setLogo("icon.png");
//		// // 配置通知栏网络图标
//		// style.setLogoUrl("");
//		// 设置通知是否响铃，震动，或者可清除
//		style.setRing(true);
//		style.setVibrate(true);
//		style.setClearable(true);
//		template.setStyle(style);
//		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
//		template.setTransmissionType(2);
//		template.setTransmissionContent("请输入您要透传的内容");
//		//设置定时展示
//		try {
//			template.setDuration("2017-10-16 17:38:00", "2017-10-17 18:28:01");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return template;
//	}

	@SuppressWarnings({ "unused", "deprecation" })
	private static void constructClientLinkMsg(String cid, String msg, IBatch batch, String URL) throws Exception {
		SingleMessage message = new SingleMessage();
		LinkTemplate template = new LinkTemplate();
		try {
			template.setDuration("2017-10-17 11:50:00", "2017-10-17 12:00:01");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTitle("title");
		template.setText("msg");
		template.setLogo("push.png");
		template.setLogoUrl("logoUrl");
		if(URL!=null && !(URL.equals(""))) {
			template.setUrl(URL);
		}
		message.setData(template);
		message.setOffline(true);
		message.setOfflineExpireTime(1 * 1000);
		// 设置推送目标，填入appid和clientId
		Target target = new Target();
		target.setAppId(appId);
		target.setClientId(cid);
		batch.add(message, target);
	}

}
