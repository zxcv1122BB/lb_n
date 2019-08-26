'use strict';

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
	return typeof obj;
} : function(obj) {
	return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var client=new Vue({
	el:"#container",
	data:{
		exportUser: base.BASE_URL,
		
		num: 1,
		pageNum: 50,
		prevStr: "",
		outOfThrity:0,
		orderState:"",

		operate_user:"",//操作员
		vipList:[],
		VIP_ID:"",//VIP等级ID
		max_coin:"",//最大
		min_coin:"",//最小

		//顶部相关统计消息
		titleMsg:[],
	},
	created:function(){
		
		this.getVipList();
		this.getTitleMsg();
	},
	mounted:function(){
		var that=this;
		$('#Authorization').val(localStorage.acessToken);
		//复制
		that.setDateTime();
		that.clickEvent();
		that.initDom();

	},
	methods:{
		initDom:function(){
			$("#recordList").on("click","td.info",function(e){
				var html="<div style='text-indent:20px;padding-top:20px;'>"+e.currentTarget.innerText+"</div>";

				layer.open({
					type: 1,
					title: '备注',
					skin: 'layui-layer-rim', //加上边框
					area: '44%', //宽高
					content: html
				  });
			})
		},
		//获取vip列表
		getVipList:function(){
			var _this = this,
			obj = {
				type: "get",
				data: {},
				url: "/userVIP/selectVips",
				success: function (data) {
					if (data.code == 200) {
						_this.vipList = data.body;
					} else {
						_this.vipList = [];
					}
				},
			};
			base.sendRequest(obj);
		},
		//获取顶部信息
		getTitleMsg: function () {
			var _this = this,
				obj = {
					type: "post",
					data: {},
					url: "/bets/getRecentlyDepositCensus",
					success: function (data) {
						if (data.code == 200) {
							_this.titleMsg = data.body;
						} else {
							_this.titleMsg = [];
						}
					},
				};
			base.sendRequest(obj);
		},
		getVal: function (elem) {
			return elem.find("option:selected").val();
		},
		//日期选择器设置
		setDateTime: function () {
			//	日期设置
			laydate.render({
				elem: '#startDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				value: "",
				done: function(value, date, endDate){
					$('.container span.b_red').removeClass('b_red');
				},
			});
			laydate.render({
				elem: '#endDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				value: "",
				done: function(value, date, endDate){
					$('.container span.b_red').removeClass('b_red');
				},
			});
		},
		//提示框
		tipsContent: function (str, times) {
			layui.use('layer', function() {
				var closeTiming = '';
				var layer = layui.layer;
				layer.msg(str, {
					time: times
				});
			});
		},
		//日期设置
		getDateTime: function (index) {
			var now = new Date(); //当前日期
			var nowDayOfWeek = now.getDay()||7; //今天本周的第几天
			var nowDay = now.getDate(); //当前日
			var nowMonth = now.getMonth(); //当前月
			var nowYear = now.getYear(); //当前年
			nowYear += nowYear < 2000 ? 1900 : 0; //
			var lastMonthDate = new Date(); //上月日期
			lastMonthDate.setDate(1);
			lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
			var lastYear = lastMonthDate.getFullYear();
			var lastMonth = lastMonthDate.getMonth();
			//格式化日期：yyyy-MM-dd
			function formatDate(date) {
				var myyear = date.getFullYear();
				var mymonth = date.getMonth() + 1;
				var myweekday = date.getDate();
				if(mymonth < 10) {
					mymonth = "0" + mymonth;
				}
				if(myweekday < 10) {
					myweekday = "0" + myweekday;
				}
				return myyear + "-" + mymonth + "-" + myweekday;
			}
			//获得某月的天数
			function getMonthDays(myMonth) {
				var monthStartDate = new Date(nowYear, myMonth, 1);
				var monthEndDate = new Date(nowYear, myMonth + 1, 1);
				var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
				return days;
			}
			//获得本季度的开始月份
			function getQuarterStartMonth() {
				var quarterStartMonth = 0;
				if(nowMonth < 3) {
					quarterStartMonth = 0;
				}
				if(2 < nowMonth && nowMonth < 6) {
					quarterStartMonth = 3;
				}
				if(5 < nowMonth && nowMonth < 9) {
					quarterStartMonth = 6;
				}
				if(nowMonth > 8) {
					quarterStartMonth = 9;
				}
				return quarterStartMonth;
			}
			//获得本周的开始日期
			function getWeekStartDate() {
				var weekStartDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek);
				return formatDate(weekStartDate);
			}
			//获得本周的结束日期
			function getWeekEndDate() {
				var weekEndDate = new Date(nowYear, nowMonth, nowDay + (7 - nowDayOfWeek));
				return formatDate(weekEndDate);
			}
			//获得上周的开始日期
			function getLastWeekStartDate() {
				var weekStartDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek - 7);
				return formatDate(weekStartDate);
			}
			//获得上周的结束日期
			function getLastWeekEndDate() {
				var weekEndDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
				return formatDate(weekEndDate);
			}
			//获得本月的开始日期
			function getMonthStartDate() {
				var monthStartDate = new Date(nowYear, nowMonth, 1);
				return formatDate(monthStartDate);
			}
			//获得本月的结束日期
			function getMonthEndDate() {
				var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
				return formatDate(monthEndDate);
			}
			//获得上月开始时间
			function getLastMonthStartDate() {
				var lastMonthStartDate = new Date(lastYear, lastMonth, 1);
				return formatDate(lastMonthStartDate);
			}
			//获得上月结束时间
			function getLastMonthEndDate() {
				var lastMonthEndDate = new Date(lastYear, lastMonth, getMonthDays(lastMonth));
				return formatDate(lastMonthEndDate);
			}
			//获得本季度的开始日期
			function getQuarterStartDate() {
				var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
				return formatDate(quarterStartDate);
			}
			//或的本季度的结束日期
			function getQuarterEndDate() {
				var quarterEndMonth = getQuarterStartMonth() + 2;
				var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));
				return formatDate(quarterStartDate);
			}
			if(index == 0) {
				var k = getWeekStartDate();
				return k;
			} else if(index == 1) {
				var k = getWeekEndDate();
				return k;
			} else if(index == 2) {
				var k = getLastWeekStartDate();
				return k;
			} else if(index == 3) {
				var k = getLastWeekEndDate();
				return k;
			} else if(index == 4) {
				var k = getMonthStartDate();
				return k;
			} else if(index == 5) {
				var k = getMonthEndDate();
				return k;
			} else if(index == 6) {
				var k = getLastMonthStartDate();
				return k;
			} else if(index == 7) {
				var k = getLastMonthEndDate();
				return k;
			}
		},
		//补0
		getzf: function (num) {
			if(parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},
		//列表模板
		setDom: function (data) {
			var list = data.body.list,
				html = "";
			if(list == '') {
				$('#page').jqPaginator('option', {
					totalPages: 1,
					currentPage: 1
				});
				$('#paging_record .sumRecord').html(0);
				html += '<tr><td colspan="13">暂无记录</td></tr>';
			} else {
				$('#page').jqPaginator('option', {
					totalPages: data.body.pages,
					currentPage:1
				});
				$('#paging_record .sumRecord').html(data.body.total);
				for(var i = 0; i < list.length; i++) {
					var obj = list[i];
					html += '<tr>'+
					'<td style="overflow:inherit;word-wrap:break-word;white-space:initial;">'+
					'<span class="c_failure personDetail" '+
					'data-name="'+(obj.userName ? obj.userName : "") +
					 '" data-index="'+(obj.id ? obj.id : "-") +'">' +
					 (obj.userName ? obj.userName : "-") + '</span><br>' 
					 + (obj.orderId ? obj.orderId : "-") + '</td>'+
					 '<td>'+(obj.payType==1?'在线支付':obj.payType==2?'银行转账':obj.payType==3?'快捷支付':'-')+'</td>'
					 +'<td>' + (obj.BANK_NAME ? obj.BANK_NAME : "-") + '</td>'+
					 '<td><span class="c_failure">' + (obj.amount ? obj.amount : "-") + '</span></td>'+
					 '<td>' + (obj.name ? obj.name : "-") + '</td>'+
					 '<td style="overflow:inherit;word-wrap:break-word;white-space:initial;">'+(obj.BANK_ACCOUNT?obj.BANK_ACCOUNT:'-')+'</td>'+
					 // '<td>' + (obj.account ? obj.account : "-") + '</td>'+
					 '<td style="overflow:inherit;word-wrap:break-word;white-space:initial;">' + (obj.applyTime ? obj.applyTime : "-") + '</td>'+
					 '<td style="overflow:inherit;word-wrap:break-word;white-space:initial;">' + (obj.operateTime ? obj.operateTime : "-") + '</td>'+
					 '<td><span class="' + (obj.depositOperateType == 2 ? 'c_failure' : 'c_success dealway') +
					 '">' + (obj.depositOperateName ? obj.depositOperateName : "-") + '</span></td>'+
					 '<td style="position:relative"><span class="' + 
					 (obj.state <= 2 ? 'c_deal' : obj.state == 3 ? 'c_success' : obj.state == 4 ? 'c_failure' : 'c_over') 
					 + '">' + (obj.stateName ? obj.stateName : "-") + '</span></td>'+
					 '<td><span data-index="' + obj.id + '" class="' +
					 (obj.state <= 2  ? 'layui-btn layui-btn-mini layui-btn-normal isLock' : '') +
					 ' ' + (obj.state == 1 ? 'lock' : '') + '">' +
					 (obj.state <= 2  ? obj.state == 2 ? "取消锁定" : '锁定' : "") + 
					 '</span>' + (obj.state == 2  ? "<br/>" : "") +
					 '<span class="' + (obj.state == 2  ? 'layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn' : '') +
					 '"  data-index="' + obj.id + '">' +
					 (obj.state ? obj.state <= 2 ? obj.state == 1 ? "" : "处理申请" : "已操作" : "-") + '<span></td>'+
					 '<td>' + (obj.operateUser ? obj.operateUser : "-") + '</td>'+
					 '<td style="cursor: pointer;overflow:inherit;word-wrap:break-word;white-space:initial;" class="info" title="'+obj.info+'">' + 
					 (obj.info ? obj.info : "-") + '</td>'+
					 '</tr>';
				}
			}
			$('#recordList').html(html);
		},
		//加载数据
		loadDataList: function(str) {
			var _this = this;
			// if(localStorage.wcz) {
			// 	str.orderState=1;
			// 	localStorage.wcz="";
			// 	_this.orderState=1;
			// 	_this.prevStr = str;
			// }
			var select = {
				type: 'get',
				url: '/memberDeposit/queryMemberDepositList',
				data: str,
				dataType: 'json',
				success: function success(data) {
					//console.log(data);
					_this.tipsContent('加载中...', 500);
					_this.setDom(data);
				},
				error: function error(XMLHttpRequest, textStatus, errorThrown) {
					//console.log(XMLHttpRequest.status);
					//console.log(XMLHttpRequest.readyState);
					//console.log(textStatus);
				}
			};
			base.sendRequest(select);
		},
			//查询按钮设置
			btn_select_click:function(){
				var _this=this,
					str = {
					// "pageIndex": _this.num,
					"pageIndex": 1,
					"pageNum": parseInt(_this.pageNum),
					"outOfThrity":parseInt(_this.outOfThrity),
					"pageSize": 10,
					"orderId": "",
					"userName": "",
					"state": 0,
					"startTime": "",
					"endTime": "",
					"depositOperateType": "",
					"payType": "",
					"agentCount": "",

					"operate_user":_this.operate_user,//操作用户
					"VIP_ID":_this.VIP_ID?_this.vipList[_this.VIP_ID-1].vipId:"",//VIP等级ID
					"max_coin":_this.max_coin,//最大
					"min_coin":_this.min_coin,//最小

				};
				if(_this.prevStr.orderState){ str.orderState=_this.prevStr.orderState;}
				str.orderId = $('#label_list input[name="rechargeId"]').val().trim() ? $('#label_list input[name="rechargeId"]').val().trim() : "";
				str.userName = $('#label_list input[name="userName"]').val().trim();
				str.depositOperateType = isNaN(parseInt(_this.getVal($("#dealWay")))) ? "" : parseInt(_this.getVal($("#dealWay")));
				str.state = isNaN(parseInt(_this.getVal($("#stateType")))) ? "" : parseInt(_this.getVal($("#stateType")));
				str.payType = isNaN(parseInt(_this.getVal($("#payType")))) ? "" : parseInt(_this.getVal($("#payType")));
				str.agentCount = $('#label_list input[name="agentCount"]').val().trim()  ? $('#label_list input[name="agentCount"]').val().trim()  : "";
				str.startTime = $('#startDate').val();
				str.endTime = $('#endDate').val();
				_this.prevStr = str;
				// console.log(str);
				var select_byDate = {
					type: 'get',
					url: '/memberDeposit/queryMemberDepositList',
					data: str,
					dataType: 'json',
					success: function success(data) {
						_this.tipsContent('加载中...', 500);
						_this.setDom(data);
					},
					error: function error(XMLHttpRequest, textStatus, errorThrown) {
						//console.log(XMLHttpRequest.status);
						//console.log(XMLHttpRequest.readyState);
						//console.log(textStatus);
					}
				};
				
				//检查最大最小金额是否符合规范
				if(parseFloat(str.max_coin)<parseFloat(_this.min_coin)){
					layer.alert('最大金额不能小于最小金额', {
						skin: 'layui-layer-molv'
						, closeBtn: 0
						, anim: 4 //动画类型
					});
					return
				}

				// 检测用户输入的时间是否符合规范（开始时间小于结束时间）或者开始时间或者结束时间
				// if (_this.checkdate(str.startTime, str.endTime)) {
					//备注--outOfThrity-1:超出,0:不超出
					if(str.startTime&&!str.endTime){//只有开始时间
						if(_this.compare(new Date(str.startTime), new Date())>7){
							select_byDate.data.outOfThrity=1;
						}else{
							select_byDate.data.outOfThrity=0;
						}
					}else if(!str.startTime&&str.endTime&&_this.checkdate(new Date(),new Date(str.endTime))){//只有结束时间
						if(_this.compare(new Date(),str.endTime)>7){
							select_byDate.data.outOfThrity=1;
						}else{
							select_byDate.data.outOfThrity=0;
						}
					}else  if(!str.startTime&&!str.endTime){//都为空，取今天
						_this.btn_date_click($("#now"),1)
						select_byDate.data.outOfThrity=0;
					}else {
						if(_this.compare(new Date(str.startTime), new Date(str.endTime))>7){
							select_byDate.data.outOfThrity=1;
						}else{
							select_byDate.data.outOfThrity=0;
						}
					}
					base.sendRequest(select_byDate);
				// }
			},
			compare:function (start,end){
				start =start.getTime();
				end =end.getTime();
				var time =0
				if(start>end){
				time =start-end;
				}else{
				time =end-start;
				}
				return Math.ceil(time/86400000)
			},
			//导出按钮
			btn_export_click:function(){
				$('#export_form').submit();
			},
			//页面显示数据条数
			selectPageNum: function() {
				var _this=this;
				$('#page').jqPaginator('option', {
					currentPage: 1
				});
				_this.num = 1;
				_this.prevStr.pageIndex = 1;
				_this.prevStr.outOfThrity = parseInt(_this.outOfThrity);
				_this.prevStr.pageNum =parseInt(_this.pageNum);
				_this.loadDataList(_this.prevStr);
			},
		//复制
		copyText:function(copyContent){
        	 // 动态创建 input 元素
  		  var aux = document.createElement("input");
  			
  		  // 获得需要复制的内容
  		  aux.setAttribute("value", copyContent);

  		  // 添加到 DOM 元素中
  		  document.body.appendChild(aux);

  		  // 执行选中
  		  // 注意: 只有 input 和 textarea 可以执行 select() 方法.
  		  aux.select();

  		  // 获得选中的内容
  		  var content = window.getSelection().toString();

  		  // 执行复制命令
  		  document.execCommand("copy");

  		  // 将 input 元素移除
  		  document.body.removeChild(aux);
  		  
  		  layer.msg('复制成功');
        },
        //处理申请弹出框
		dealApplicationLayer: function (id) {
			var that = this;
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					skin:'confirm-class',
					title: '处理会员充值申请',
					content: $('#dealApplication'),
					area: '40%',
					btn: ['关闭','保存'],
					yes: function (index, layero) {
						layer.closeAll('page');
					},
					btn2: function (index, layero) {
						var str = {
							id: id,
							state: parseInt(that.getVal($('#changeState'))),
							info:$('#dealApplication .info').val(),
							amount: $('#dealApplication .amount').val(),
						},re = /^[0-9]+.?[0-9]*$/;
						//console.log(parseInt(str.amount));
						if(!re.test(str.amount)){
							that.tipsContent('充值金额只能为数字',500);
						}else{
							str.amount=parseFloat(str.amount);
							var changeState = {
								type: 'post',
								data: str,
								url: '/memberDeposit/depositHandle',
								success: function (data) {
									layer.closeAll('page');
									if(data.code == 200) {
										that.tipsContent('处理完成', 500);
										that.loadDataList(that.prevStr);
									} else {
										that.tipsContent(data.msg, 1000);
									}
								}
							};
							base.sendRequest(changeState);
						}
						return false
						
					}
				});
			});
		},
		//会员详情展示
		personDetail: function (list) {
			//console.log(list);
			var html = "";
			if(list.body) {
				var obj = list.body;
				html += '<table class="layui-table"><colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup><tbody> <td style="text-align: right;">\u767B\u5F55\u5E10\u53F7:</td><td style="text-align: left;">' + (obj.userName ? obj.userName : '-') + '</td><td style="text-align: right;">\u6240\u5C5E\u4E0A\u7EA7:</td><td style="text-align: left;">' + (obj.proxyName ? obj.proxyName : '-') + '</td></tr><tr><td style="text-align: right;">\u4F1A\u5458\u59D3\u540D:</td><td style="text-align: left;">' + (obj.fullName ? obj.fullName : '-') + '</td><td style="text-align: right;">类别:</td><td style="text-align: left;">' + (obj.userType == 1 ? '代理' : '会员') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u4F1A\u5458\u4F59\u989D:</td><td style="text-align: left;">' + (obj.coin ? obj.coin : '-') + '</td><td style="text-align: right;">\u79EF\u5206:</td><td style="text-align: left;">' + (obj.score ? obj.score : '0') + '</td></tr><tr><td style="text-align: right;">\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsum ? obj.betsum : '0.00') + '</td><td style="text-align: right;">\u51FA\u6B3E\u6240\u9700\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsumNeed ? obj.betsumNeed : '0.00') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u72B6\u6001:</td><td style="text-align: left;">' + (obj.status ? obj.status == 2 ? '冻结' : '正常' : '-') + '</td><td style="text-align: right;">\u4F1A\u5458\u7B49\u7EA7:</td><td style="text-align: left;">' + (obj.vipName ? obj.vipName : '-') + '</td></tr><tr class="getMoreMsg" rowspan="2" style="background:#666;color:#fff;cursor:pointer"><td colspan="4"><span>\u5C55\u5F00\u663E\u793A\u66F4\u591A<span></td></tr><tr class="hide"><td style="text-align: right;">手机号:</td><td style="text-align: left;">' + (obj.phoneNumber ? obj.phoneNumber : '-') + '</td><td style="text-align: right;">\u5FAE\u4FE1\u53F7:</td><td style="text-align: left;">' + (obj.weixin ? obj.weixin : '-') + '</td></tr><tr class="odd_row hide"><td style="text-align: right;">LINE:</td><td style="text-align: left;">' + (obj.line? obj.line: '-') + '</td><td style="text-align: right;">\u90AE\u7BB1:</td><td style="text-align: left;">' + (obj.email ? obj.email : '-') + '</td></tr><tr><td style="text-align: right;">银行帐号:</td><td colspan="3" style="text-align: left;"><span>' + (obj.bankAccount ? obj.bankAccount : '-') + '</span><span data-text="' + obj.bankAccount + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span><br/><span class="layui-btn layui-btn-mini ' + (obj.bankBlacklistStatus == 0 ? 'layui-btn-danger' : '') + '">' + (obj.bankBlacklistStatus ? obj.bankBlacklistStatus == 1 ? '可用' : '禁用' : '') + '</sapn></td></tr><tr class="odd_row"><td style="text-align: right;">银行名称:</td><td style="text-align: left;">' + (obj.bankName ? obj.bankName : '-') + '</td><td style="text-align: right;">开户分行:</td><td style="text-align: left;">' + (obj.bankAddress ? obj.bankAddress : '-') + '</td></tr><tr><td style="text-align: right;">\u6CE8\u518CIP:</td><td style="text-align: left;">' + (obj.regIp ? obj.regIp : '-') + '</td><td style="text-align: right;">\u6CE8\u518C\u6765\u6E90:</td><td style="text-align: left;">' + (obj.channel ? (obj.channel==1?"pc":(obj.channel==2?"wap":(obj.channel==3?"app":" 管理员添加"))) : '-')+ '</td></tr><tr class="odd_row"><td style="text-align: right;">\u6CE8\u518C\u7CFB\u7EDF:</td><td style="text-align: left;">' + (obj.regSystem ? obj.regSystem : '-') + '</td><td style="text-align: right;">\u6700\u540E\u767B\u5F55\u65F6\u95F4:</td><td style="text-align: left;">' + (obj.lastLoginTime ? obj.lastLoginTime : '-') + '</td></tr><tr><td style="text-align: right;">\u5907\u6CE8:</td><td colspan="3" style="text-align: left;">' + (obj.info ? obj.info : '-') + '</td></tr> </tbody></table>';
			} else {
				html = '<p>\u627E\u4E0D\u5230\u8BE5\u4F1A\u5458\u4FE1\u606F</p>';
			}

			$('#personDetail').html(html);
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '会员详情',
					area: ['60%', '95%'],
					content: $('#personDetail'),
					btn: ['确定'],
					yes: function yes(index, layero) {
						layer.closeAll('page');
					}
				});
			});
		},
        //日期按钮
		btn_date_click: function(event, index) {
			if(event){
				if(event.currentTarget){
					event = event.currentTarget;
				}
				
				$('.container span.b_red').removeClass('b_red');
				$(event).addClass('b_red');
			}
			
			var dateTime = new Date(),
				st, et,that=this;
			switch(index) {
				case 1: //今天
					dateTime.setTime(dateTime.getTime());
					st = dateTime.getFullYear() + "-" + that.getzf(dateTime.getMonth() + 1) + "-" + that.getzf(dateTime.getDate());
					et = st;
					break;
				case 2: //昨天
					dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
					st = dateTime.getFullYear() + "-" + that.getzf(dateTime.getMonth() + 1) + "-" + that.getzf(dateTime.getDate());
					et = st;
					break;
				case 3: //本周
					st = that.getDateTime(0),
						et = dateTime.getFullYear() + "-" + that.getzf(dateTime.getMonth() + 1) + "-" + that.getzf(dateTime.getDate());
					break;
				case 4: //上周
					st = that.getDateTime(2),
						et = that.getDateTime(3);
					break;
				case 5: //本月
					st = that.getDateTime(4),
						et = dateTime.getFullYear() + "-" + that.getzf(dateTime.getMonth() + 1) + "-" + that.getzf(dateTime.getDate());
					break;
				case 6: //上月
					st = that.getDateTime(6),
						et = that.getDateTime(7);
					break;
			}
			$("#startDate").val(st + " " + "00:00:00");
			$("#endDate").val(et + " " + "23:59:59");
		},
		//相关点击事件
		clickEvent: function () {
			var _this = this;

			function getVal(elem) {
				return elem.find("option:selected").val();
			}
			//处理请求弹出框
			$('#xstck').click(function() {
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.open({
						type: 1,
						title: '处理申请',
						area: ['50%','70%'],
						content: $('#dealApplication') //这里content是一个普通的String
					});
					$('#save').on('click', function() {
						layer.closeAll('page');
					});
				});
			});
			//手動處理按鈕
			$('#recordList').on('click', '.dealway', function() {
				$('.dealwayContent').addClass('hide');
				if($(this).parent().next().children('.dealwayContent').is('.hide')) {
					$(this).parent().next().children('.dealwayContent').removeClass('hide');
				} else {
					$(this).parent().next().children('.dealwayContent').addClass('hide');
				}
			});
			//会员详情点击按钮
			$('#recordList').on('click', '.personDetail', function() {
				var str = {
					userName: $(this).attr("data-name"),
				};
				var userDetail = {
					type: 'post',
					data: str,
					url: '/memberDeposit/depositUserInfo',
					success: function (data) {
						if(data.code==200){
							if(data.body){
								_this.personDetail(data);
							}else{
								_this.tipsContent("找不到该会员",1000);
							}
						}
						
					}
				};
				base.sendRequest(userDetail);
			});
			//处理状态切换
			$('#recordList').on('click', '.isLock', function() {
				var str = {
					id: parseInt($(this).attr('data-index')),
					state: ($(this).is('.lock'))?2:1
				},changeState = {
					type: 'post',
					data: str,
					url: '/memberDeposit/depositIsLock',
					success: function (data) {
						if(data.code==200){
							var str = {
								"pageIndex": _this.num,
								"pageNum": _this.pageNum,
								"pageSize": 10
							};
							if(_this.prevStr != "") {
								str = _this.prevStr;
							}
//							_this.loadDataList(str);
							if(state==2){
								$(selfThis).removeClass('lock');
								$(selfThis).html('取消锁定');
								$(selfThis).next().addClass('layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn');
								$(selfThis).next().html('处理申请');
								$(selfThis).append('<br/>');
							}else{
								$(selfThis).addClass('lock');
								$(selfThis).html('锁定');
								$(selfThis).parent().find('br').remove();
								$(selfThis).parent().find('.dealApplicationBtn').html("");
								$(selfThis).parent().find('.dealApplicationBtn').removeClass('layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn');
							}
							
							_this.tipsContent('刷新中', 400);	
						}else{
							_this.tipsContent(data.msg, 400);	
						}
						
					}
				},selfThis=this,state=($(this).is('.lock'))?2:1;
				layui.use('layer',function(){
					var layer = layui.layer;
					layer.open({
						type: 1,
						skin:'confirm-class',
						title: '确认操作',
						area: '20%',
						content: '<div style="padding: 10px 20px;color: #e4393c;">'+(str.state==2?'是否锁定':'是否取消锁定')+'</div>',
						btn: ['取消', '确定'],
						yes: function() {
							layer.closeAll('page');
						},
						btn2:function(){
							base.sendRequest(changeState);
						},
					});
				});
			});
			//处理申请
			$('#recordList').on('click', '.dealApplicationBtn', function() {
				var id = parseInt($(this).attr('data-index'));
				var str = {
					id: id
				};
				var selectMsg = {
					type: 'get',
					data: str,
					url: '/memberDeposit/depositQuery',
					success: function(data) {
						var html = "";
							if(data) {
								var obj = data;
								sessionStorage.userName = obj.userName;	
								html += '<table class="layui-table detail"><colgroup><col width="30%" /><col width="70%" /></colgroup><tbody><tr><td class="tr" style="text-align:  right;">会员账号:</td><td style="text-align:  left;"><span>' + (obj.userName ? obj.userName : "-") + '</span><span data-text="' + (obj.userName ? obj.userName : "") + '" class="copyBtn layui-btn layui-btn-mini">复制</span></td></tr><tr><td class="tr" style="text-align:  right;">存款人姓名:</td><td style="text-align:  left;"><span>' + (obj.accountName ? obj.accountName : "-") + '</span><span data-text="' + (obj.accountName ? obj.accountName : "") + '" class="copyBtn layui-btn layui-btn-mini">复制</span></td></tr><tr><td class="tr" style="text-align:  right;">转账类型:</td><td style="text-align:  left;"><span>' + (obj.payName ? obj.payName : "-") + '</span></td></tr>' + (obj.payType != 3 ? ('<tr><td class="tr" style="text-align:  right;">微信号|支付宝:</td><td style="text-align:  left;"><span>' + (obj.account ? obj.account : "-") + '</span></td></tr>') : '') + '<tr><td class="tr" style="text-align:  right;">充值金额:</td><td style="text-align:  left;"><input type="text" class="amount" value="' + (obj.amount ? obj.amount : "0") + '"/></td></tr><tr><td class="tr" style="text-align:  right;">手续费:</td><td style="text-align:  left;"><input type="text" value="0" disabled/></td></tr><tr><td class="tr" style="text-align:  right;">处理结果:</td><td colspan="3" style="text-align:  left;"><select name="" id="changeState"><option value="3">\u6279\u51C6</option><option value="4">\u9A73\u56DE</option></select></td></tr><tr><td class="tr" style="text-align:  right;">备注:</td><td style="text-align:  left;"><input type="text" class="info" value="' + (obj.info ? obj.info : "-") + '"/></td></tr></tbody></table>';
								
							} else {
								html += '';
							}
							$('#dealApplication').html(html);
							_this.dealApplicationLayer(id);
					}
				};
				base.sendRequest(selectMsg);
			});
			//复制
			$("body").on('click','.copyBtn',function(){
				_this.copyText($(this).attr('data-text'));
			});
			//会员详情弹出更多
			$('#personDetail').on('click', '.getMoreMsg', function() {
				$('#personDetail .hide').removeClass('hide');
				$(this).addClass('hide');
			});
		},
		// 检测时间的先后顺序
		checkdate:function(start,end){   
			//得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
			//再进行判断就好判断了
			var sDate = new Date(start.replace(/\-/g, "\/"));
			var eDate = new Date(end.replace(/\-/g, "\/"));
			if(sDate > eDate) {
				layer.alert('结束时间不能早于开始时间', {
					skin: 'layui-layer-molv'
					, closeBtn: 0
					, anim: 4 //动画类型
				});
				return false;
			}
			return true;
		},
	},
	watch:{
		pageNum:function(){
			this.selectPageNum();
		},
	},
});
$(function() {
	//初始化时间为今天
	client.btn_date_click($("#now"),1)
	//分页
	$.jqPaginator('#page', {
		totalPages: 1, //多少页数据
		visiblePages: 10, //最多显示几页
		currentPage: 1, //当前页
		wrapper: '<ul class="pagination"></ul>',
		first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

		onPageChange: function onPageChange(num) {
			client.num = num;
			var str = {
				"pageIndex": client.num,
				"pageNum": parseInt(client.pageNum),
				"pageSize": 10,
				// "outOfThrity": parseInt(client.outOfThrity),
			};
			if(client.prevStr == "") {
				client.prevStr = str;
			} else {
				client.prevStr.pageIndex = num;
				str = client.prevStr;
			}
			if(num==1){ //今天
				var dateTime = new Date(),st, et;
					dateTime.setTime(dateTime.getTime());
					st = dateTime.getFullYear() + "-" + client.getzf(dateTime.getMonth() + 1) + "-" + client.getzf(dateTime.getDate());
					et = st;
					str.startTime=st + " " + "00:00:00";
					str.endTime=et + " " + "23:59:59";
			}
			client.loadDataList(str);
		}
	});
});
