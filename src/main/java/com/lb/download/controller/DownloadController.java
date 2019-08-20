package com.lb.download.controller;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.service.IPreferentialCardService;
import com.lb.download.model.DepositRecord;
import com.lb.download.model.PreferentialCardRecord;
import com.lb.download.model.UserDownload;
import com.lb.download.model.WithdrawRecord;
import com.lb.excel.ExportExcel;
import com.lb.member.service.IMemberDepositService;
import com.lb.member.service.IMemberWithdrawService;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.FileOperator;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.ResponseUtils;

/***
 * 下载控制器
 * 
 * @author ASUS
 */
@RestController
@RequestMapping("/download")
public class DownloadController extends BaseController {
	@Autowired
	private UserModelService userService;
	@Autowired
	private IMemberDepositService memberDepositService;
	@Autowired
	private IMemberWithdrawService memberWithdrawService;
	@Autowired
	private IPreferentialCardService preferentialCardService;
	/***
	 * 导出会员的信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportUserList")
	public ModelAndView exportUserList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = this.getParam(request);
		try {
			List<UserDownload> allUser = userService.queryUserListDownl(map);
			ExportExcel<UserDownload> ee = new ExportExcel<UserDownload>();
			// 头信息
			String[] headers = { "会员账号", "会员姓名", "余额", "会员等级", "注册时间", "最近登录时间", "最后登录IP", "手机号码", "qq", "微信", "邮箱",
					"银行账号", "取现银行名称", "银行地址", "注册IP" };
			// 默认的表名字
			String fileName = "用户信息表";
			ee.exportExcel(headers, allUser, fileName, response);
			return ResponseUtils.jsonView(200, "导出成功");
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "导出失败");
		}
	}

	
	/***
	 * app下载
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadApp", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=雅彩.apk");
        //String path = request.getSession().getServletContext().getRealPath("/app/H54157FDE_1014154239Test.apk");
        String path =File.separator+"bbt";
        OutputStream outputStream = response.getOutputStream();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        response.setHeader("Content-Length", file.length() + "");
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        byte[] buffer = new byte[bis.available()];
        int i = -1;
        while ((i = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, i);
        }
        bos.flush();
        bos.close();
        bis.close();
        outputStream.close();
        inputStream.close();*/
		String path=GetPropertiesValue.getValueArray("URL","downloadPath")[0];
		String name=GetPropertiesValue.getValueArray("URL","appName")[0];
		FileOperator.downLoadFile(path,name,response);
	}

	/***
	 * 导出提现记录
	 * 
	 * @param request	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportDepositRecord")
	public ModelAndView exportDepositRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = this.getParam(request);
		try {
			List<DepositRecord> allUser = memberDepositService.exportDepositRecord(map);
			ExportExcel<DepositRecord> ee = new ExportExcel<DepositRecord>();
			// 头信息
			String[] headers = { "账号", "订单号", "支付平台", "付款金额", "银行名字", "存款人姓名", "账号后10位", "申请时间", "处理时间", "处理类型",
					"状态", "操作人", "备注" };
			// 默认的表名字
			String fileName = "充值记录表";
			ee.exportExcel(headers, allUser, fileName, response);
			return ResponseUtils.jsonView(200, "导出成功");
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "导出失败");
		}
	}

	/***
	 * 导出提现记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportWithdrawRecord")
	public ModelAndView exportWithdrawRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = this.getParam(request);
		try {
			List<WithdrawRecord> allUser = memberWithdrawService.exportWithdrawRecord(map);
			ExportExcel<WithdrawRecord> ee = new ExportExcel<WithdrawRecord>();
			// 头信息
			String[] headers = { "账号", "订单号", "用户类型", "提现金额", "银行名字", "存款人姓名", "账号后10位", "申请时间", "处理时间", "状态", "操作人",
					"备注" };
			// 默认的表名字
			String fileName = "提现记录表";
			ee.exportExcel(headers, allUser, fileName, response);
			return ResponseUtils.jsonView(200, "导出成功");
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "导出失败");
		}
	}
	/***
	 * 导出提现记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportPreferentialCard")
	public ModelAndView exportPreferentialCard(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = this.getParam(request);
		try {
			List<PreferentialCardRecord> allUser = preferentialCardService.exportPreferentialCard(map);
			ExportExcel<PreferentialCardRecord> ee = new ExportExcel<PreferentialCardRecord>();
			// 头信息
			String[] headers = { "批次", "序号", "设备", "试用vip", "优惠卡账号", "优惠卡密码", "优惠卡面额", "优惠卡启用状态", "优惠卡使用状态", "优惠卡开始时间", "优惠卡结束时间",
			"创建时间","更新时间","使用者账号"};
			// 默认的表名字
			String fileName = "优惠卡";
			ee.exportExcel(headers, allUser, fileName, response);
			return ResponseUtils.jsonView(200, "导出成功");
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "导出失败");
		}
	}
}
