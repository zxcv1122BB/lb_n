package com.lb.sys.tools.model;
import java.io.IOException;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class MyBatchPushDemo {
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
	private static String appId = "iAQ6wGElV9AwCVsc2IjBY1";
	private static String appKey = "Q319BboQeNAn5iltT4W4j5";
	@SuppressWarnings("unused")
	private static String masterSecret = "xVhn0081SwAR79uxaN6Wk4";
	@SuppressWarnings("unused")
	private static String url = "http://sdk.open.api.igexin.com/apiex.htm";// 向所有app下的用户推送
	@SuppressWarnings("unused")
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";// 向CID推送

    static String CID_A = "a212286963d9cb76a08f274d8c07995e";
    static String CID_B = "4caa8ffe1160c460246c8c09d3aedd98";
    //别名推送方式
    // static String Alias = "";

    public static void main(String[] args) throws IOException {
    	/***
    	 * 群推消息，打开Url
    	 *   IIGtPush push = new IGtPush(host, appKey, masterSecret);
        IBatch batch = push.getBatch();

        try {
            //构建客户a的透传消息a
        	constructClientLinkMsg(CID_A,"消息1",batch);
            //构建客户B的点击通知打开网页消息b
        	constructClientLinkMsg(CID_B,"消息2",batch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IPushResult submit = batch.submit();
    	 */

    }

    @SuppressWarnings("unused")
	private static void constructClientTransMsg(String cid, String msg ,IBatch batch) throws Exception {

        SingleMessage message = new SingleMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(msg);
        template.setTransmissionType(2); // 这个Type为int型，填写1则自动启动app

        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1 * 1000);

        // 设置推送目标，填入appid和clientId
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cid);
        batch.add(message, target);
    }

    //给CID传递数据
    @SuppressWarnings({ "unused", "deprecation" })
	private static void constructClientLinkMsg(String cid, String msg ,IBatch batch,String URL) throws Exception {
        SingleMessage message = new SingleMessage();
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle("title");
        template.setText("msg");
        template.setLogo("push.png");
        template.setLogoUrl("logoUrl");
        template.setUrl(URL);
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