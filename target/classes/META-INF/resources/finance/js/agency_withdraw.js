'use strict';

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
	return typeof obj;
} : function(obj) {
	return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var withdrawals = new Vue({
	el: "#container",
	data: {
		num: 1,
		pageNum: 50,
		prevStr: '',
		selectStr: '',
		outOfThrity: 0,
		dealState:'',
		exportUser: base.BASE_URL,
	},
	created: function() {},
	mounted: function() {
		$('#Authorization').val(localStorage.acessToken);
		this.setDateTime();
		this.clickEvent();
	},
	methods: {
		//补0
		getzf: function(num) {
			if(parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},
		getVal: function(elem) {
			return elem.find("option:selected").val();
		},
		//日期选择器
		setDateTime: function() {
			//	日期设置
			laydate.render({
				elem: '#startDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				value: "",
				position: 'fixed',
				done: function(value, date, endDate) {
					$('.container span.b_red').removeClass('b_red');
				},
			});
			laydate.render({
				elem: '#endDate', //指定元素
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',
				value: "",
				position: 'fixed',
				done: function(value, date, endDate) {
					$('.container span.b_red').removeClass('b_red');
				},

			});
		},
		//日期设置
		getDateTime: function(index) {
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
		//提示框
		tipsContent: function(str, times) {
			layui.use('layer', function() {
				var closeTiming = '';
				var layer = layui.layer;
				layer.msg(str, {
					time: times
				});
			});
		},
		//加载
		loadDataList: function(str) {
			str.userType = 1;
			var _this = this,
				select_byName = {
					type: 'get',
					url: '/memberWithdraw/queryMemberWithdrawList',
					data: str,
					dataType: 'json',
					success: function(data) {

						var html = '';
						if(data.body.list == '') {
							$('#page').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
							$('#paging_record .sumRecord').html(0);
							html += '<tr><td colspan="13">\u6682\u65E0\u8BB0\u5F55</td></tr>';
						} else {
							$('#page').jqPaginator('option', {
								totalPages: data.body.pages
							});
							$('#paging_record .sumRecord').html(data.body.total);
							for(var i = 0; i < data.body.list.length; i++) {
								var obj = data.body.list[i];
								html += '<tr><td><span class="c_failure personDetail"  data-name="' + (obj.userName ? obj.userName : "") + '" data-index="' + (obj.id ? obj.id : "-") + '">' + (obj.userName ? obj.userName : "-") + '</span><br>' + (obj.orderId ? obj.orderId : "-") + '</td><td>' + (obj.accountName ? obj.accountName : "-") + '</td><td>' + (obj.accountId ? obj.accountId : "-") + '</td><td>' + (obj.amount ? obj.amount : "-") + '</td><td title="'+(obj.applyTime ? obj.applyTime : "") +'">' + (obj.applyTime ? obj.applyTime : "-") + '</td><td title="'+(obj.operateTime ? obj.operateTime : "") +'">' + (obj.operateTime ? obj.operateTime : "-") + '</td><td style="position:relative"><span class="' + (obj.state <= 2 ? 'c_deal' : obj.state == 3 ? 'c_success' : obj.state == 4 ? 'c_failure' : 'c_over') + '">' + (obj.stateName ? obj.stateName : "-") + '</span><td><span data-index="' + obj.id + '" class="' + (obj.state <= 2 ? 'layui-btn layui-btn-mini layui-btn-normal isLock' : '') + ' ' + (obj.state == 1 ? 'lock' : '') + '" data-index="">' + (obj.state <= 2 ? obj.state == 2 ? "取消锁定" : '锁定' : "") + '</span>' + (obj.state == 2 ? "<br/>" : "") + '<span class="' + (obj.state == 2 ? 'layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn' : '') + '" data-index="' + obj.id + '">' + (obj.state ? obj.state <= 2 ? obj.state == 1 ? "" : "处理申请" : "已操作" : "-") + '<span></td><td>' + (obj.operateUser ? obj.operateUser : "-") + '</td><td>' + (obj.info ? obj.info : "-") + '</td></tr>';
							}
						}
						$('#recordList').html(html);
					},
					error: function error(e) {
						//console.log(e);
					}
				};
			base.sendRequest(select_byName);
		},
		loadingDate: function(str) {
			str.userType = 1;
			var _this = this,
				select_byName = {
					type: 'get',
					url: '/memberWithdraw/queryMemberWithdrawList',
					data: str,
					dataType: 'json',
					success: function(data) {
						var html = '';
						if(data.body.list == '') {
							$('#page').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
							$('#paging_record .sumRecord').html(0);
							html += '<tr><td colspan="13">\u6682\u65E0\u8BB0\u5F55</td></tr>';
						} else {
							$('#page').jqPaginator('option', {
								totalPages: data.body.pages
							});
							$('#paging_record .sumRecord').html(data.body.total);
							for(var i = 0; i < data.body.list.length; i++) {
								var obj = data.body.list[i];
								html += '<tr><td><span class="c_failure personDetail"  data-name="' + (obj.userName ? obj.userName : "") + '" data-index="' + (obj.id ? obj.id : "-") + '">' + (obj.userName ? obj.userName : "-") + '</span><br>' + (obj.orderId ? obj.orderId : "-") + '</td><td>' + (obj.accountName ? obj.accountName : "-") + '</td><td>' + (obj.accountId ? obj.accountId : "-") + '</td><td>' + (obj.amount ? obj.amount : "-") + '</td><td>' + (obj.applyTime ? obj.applyTime : "-") + '</td><td>' + (obj.operateTime ? obj.operateTime : "-") + '</td><td style="position:relative"><span class="' + (obj.state <= 2 ? 'c_deal' : obj.state == 3 ? 'c_success' : obj.state == 4 ? 'c_failure' : 'c_over') + '">' + (obj.stateName ? obj.stateName : "-") + '</span><td><span data-index="' + obj.id + '" class="' + (obj.state <= 2 ? 'layui-btn layui-btn-mini layui-btn-normal isLock' : '') + ' ' + (obj.state == 1 ? 'lock' : '') + '" data-index="">' + (obj.state <= 2 ? obj.state == 2 ? "取消锁定" : '锁定' : "") + '</span>' + (obj.state == 2 ? "<br/>" : "") + '<span class="' + (obj.state == 2 ? 'layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn' : '') + '" data-index="' + obj.id + '">' + (obj.state ? obj.state <= 2 ? obj.state == 1 ? "" : "处理申请" : "已操作" : "-") + '<span></td><td>' + (obj.operateUser ? obj.operateUser : "-") + '</td><td>' + (obj.info ? obj.info : "-") + '</td></tr>';
							}
						}
						$('#recordList').html(html);
					}
				};
			base.sendRequest(select_byName);
		},
		//查询
		btn_select_click: function() {
			var _this = this,
				str = {
					"pageIndex": 1,
					"pageNum": parseInt(_this.pageNum),
					"pageSize": 10,
					"outOfThrity":parseInt(_this.outOfThrity),
					"agentCount": "",
					"orderId": "",
					"state":parseInt(_this.dealState),
					"userName": "",
					"startTime": "",
					"endTime": ""
				};
			str.userName = $('#label_list input[name="userName"]').val().trim() ? $('#label_list input[name="userName"]').val().trim() : "";
			str.orderId = $('#label_list input[name="rechargeId"]').val().trim() ? $('#label_list input[name="rechargeId"]').val().trim() : "";
			str.agentCount = $('#label_list input[name="agentCount"]').val().trim() ? $('#label_list input[name="agentCount"]').val().trim() : "";
			str.startTime = $('#startDate').val();
			str.endTime = $('#endDate').val();
			//console.log(str.startTime);
			//console.log(str.endTime);
			str.userType = 1;
			_this.prevStr = str;
			//console.log(str);
			var select_byName = {
				type: 'get',
				url: '/memberWithdraw/queryMemberWithdrawList',
				data: str,
				dataType: 'json',
				success: function(data) {
					var html = '';
					if(!data.body.list || data.body.list.length == 0) {
						$('#page').jqPaginator('option', {
							totalPages: 1,
							currentPage: 1
						});
						$('#paging_record .sumRecord').html(0);
						html += '<tr><td colspan="13">\u6682\u65E0\u8BB0\u5F55</td></tr>';
					} else {
						$('#page').jqPaginator('option', {
							totalPages: data.body.pages,
							currentPage: 1
						});
						$('#paging_record .sumRecord').html(data.body.total);
						for(var i = 0; i < data.body.list.length; i++) {
							var obj = data.body.list[i];
							html += '<tr><td><span class="c_failure personDetail"  data-name="' + (obj.userName ? obj.userName : "") + '" data-index="' + (obj.id ? obj.id : "-") + '">' + (obj.userName ? obj.userName : "-") + '</span><br>' + (obj.orderId ? obj.orderId : "-") + '</td><td>' + (obj.accountName ? obj.accountName : "-") + '</td><td>' + (obj.accountId ? obj.accountId : "-") + '</td><td>' + (obj.amount ? obj.amount : "-") + '</td><td>' + (obj.applyTime ? obj.applyTime : "-") + '</td><td>' + (obj.operateTime ? obj.operateTime : "-") + '</td><td style="position:relative"><span class="' + (obj.state <= 2 ? 'c_deal' : obj.state == 3 ? 'c_success' : obj.state == 4 ? 'c_failure' : 'c_over') + '">' + (obj.stateName ? obj.stateName : "-") + '</span><td><span data-index="' + obj.id + '" class="' + (obj.state <= 2 ? 'layui-btn layui-btn-mini layui-btn-normal isLock' : '') + ' ' + (obj.state == 1 ? 'lock' : '') + '" data-index="">' + (obj.state <= 2 ? obj.state == 2 ? "取消锁定" : '锁定' : "") + '</span>' + (obj.state == 2 ? "<br/>" : "") + '<span class="' + (obj.state == 2 ? 'layui-btn layui-btn-mini layui-btn-normal dealApplicationBtn' : '') + '" data-index="' + obj.id + '">' + (obj.state ? obj.state <= 2 ? obj.state == 1 ? "" : "处理申请" : "已操作" : "-") + '<span></td><td>' + (obj.operateUser ? obj.operateUser : "-") + '</td><td>' + (obj.info ? obj.info : "-") + '</td></tr>';
						}
					}
					$('#recordList').html(html);
				},
				error: function(e) {
					//console.log(e);
				}
			};
			// 检测用户输入的时间是否符合规范（开始时间小于结束时间）
			if(_this.checkdate(str.startTime, str.endTime)) {
				base.sendRequest(select_byName);
			}
		},
		//导出按钮
		btn_export_click: function() {
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
			_this.prevStr.pageNum = parseInt(_this.pageNum);
			_this.loadDataList(_this.prevStr);
		},
		//日期按钮
		btn_date_click: function(event, index) {
			event = event.currentTarget;
			$('.container span.b_red').removeClass('b_red');
			$(event).addClass('b_red');
			var dateTime = new Date(),
				st, et, that = this;
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
		clickEvent: function() {
			var _this = this;
			//	弹出框设置
			$('#add').on('click', function() {
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.open({
						type: 1,
						area: ['50%', '30%'],
						content: $('#dealApplication')
					});
				});
			});

			//处理申请按钮
			$('#recordList').on('click', '.dealApplicationBtn', function() {
				var id = parseInt($(this).attr('data-index'));
				var str = {
					id: id,
					userType: 1,
				};
				var sendDealApplication = {
					type: 'get',
					data: str,
					url: '/memberWithdraw/withdrawQuery',
					success: function(data) {
						if(data) {
							var html = "",
								obj = data;
							sessionStorage.userName = obj.userName;
							_this.dealApplication_status=obj.status;
							html += '<table class="layui-table detail"><colgroup><col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" /></colgroup><tbody><tr><td class="tr">代理账号</td><td><span>' + (obj.userName ? obj.userName : "-") + '</span><span data-text="' + (obj.userName ? obj.userName : "") + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td><td class="tr">代理状态：</td><td>' + (obj.status ? obj.status==1?'正常':'禁用' : "-") + '</td></tr><tr><td class="tr">\u6536\u6B3E\u94F6\u884C\uFF1A</td><td><span>' + (obj.accountId ? obj.accountId : "-") + '</span><span data-text="' + (obj.accountId ? obj.accountId : "") + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td><td class="tr">\u6536\u6B3E\u6237\u540D\uFF1A</td><td><span class="copy">' + (obj.accountName ? obj.accountName : "-") + '</span><span data-text="' + (obj.accountName ? obj.accountName : "") + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td></tr><tr><td class="tr">\u6536\u6B3E\u94F6\u884C\u8D26\u53F7\uFF1A</td><td style="position:relative"><span class="copyContent">' + (obj.account ? obj.account : "-") + '</span><span data-text="' + (obj.account ? obj.account : "") + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td><td class="tr">\u53D6\u6B3E\u65F6\u95F4\uFF1A</td><td>' + (obj.applyTime ? obj.applyTime : "-") + '</td></tr><tr><td class="tr">\u7533\u8BF7\u91D1\u989D\uFF1A</td><td><span class="copyContent c_red">' + (obj.amount ? obj.amount : "0") + '</span><span data-text="' + (obj.amount ? obj.amount : "0") + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td><td class="tr">\u53D6\u6B3E\u603B\u989D(\u6B21\u6570)\uFF1A</td><td>' + (obj.withdrawAmountSum ? obj.withdrawAmountSum : "0") + '(' + (obj.withdrawSum ? obj.withdrawSum : "0") + ')</td></tr><tr><td class="tr">\u7528\u6237\u6CE8\u518C\u65F6\u95F4\uFF1A</td><td>' + (obj.regTime ? obj.regTime : "-") + '</td><td class="tr">\u624B\u52A8\u5904\u7406\u5145\u503C<br>\u91D1\u989D(\u6B21\u6570)\uFF1A</td><td><span class="c_red">' + (obj.depositAmountRGSum ? obj.depositAmountRGSum : "0") + '(' + (obj.depositRGSum ? obj.depositRGSum : "0") + ')</span></td></tr><tr><td class="tr">\u5904\u7406\u7ED3\u679C\uFF1A</td><td colspan="3"><select name="" id="changeState"><option value="3">\u6279\u51C6</option><option value="4">\u9A73\u56DE</option></select></td></tr> <tr><td style="text-align:right;">\u5907\u6CE8:</td><td colspan="3"><input style="width: 100%; padding: 4px 8px; box-sizing: border-box;"class="info"/></td></tr></tbody></table><div><p class="c_red">\u63D0\u793A: \u5145\u503C\u3001\u63D0\u6B3E\u6570\u636E\u6765\u6E90\u4E8E\u6700\u540E\u4E00\u6B21\u4F1A\u5458\u6210\u529F\u53D6\u6B3E\u7684\u65F6\u95F4()\u5230\u5F53\u524D\u65F6\u95F4\u7684\u6570\u636E</p></div>';
						} else {
							html = '<p>\u627E\u4E0D\u5230\u8BE5\u5904\u7406\u7533\u8BF7</p>';
						}
						$('#dealApplication').html(html);
						_this.dealApplication(id);
					}
				};
				base.sendRequest(sendDealApplication);
			});
			//处理状态切换
			$('#recordList').on('click', '.isLock', function() {
				var str = {
						id: parseInt($(this).attr('data-index')),
						state: ($(this).is('.lock')) ? 2 : 1
					},
					changeState = {
						type: 'post',
						data: str,
						url: '/memberWithdraw/withdrawIsLock',
						success: function(data) {
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
						}
					},selfThis=this,state=($(this).is('.lock'))?2:1;
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.open({
						type: 1,
						skin: 'confirm-class',
						title: '确认操作',
						area: '20%',
						content: '<div style="padding: 10px 20px;color: #e4393c;">' + (str.state == 2 ? '是否锁定' : '是否取消锁定') + '</div>',
						btn: ['取消', '确定'],
						yes: function() {
							layer.closeAll('page');
						},
						btn2: function() {
							base.sendRequest(changeState);
						},
					});
				});
			});
			//会员详情点击按钮
			$('#recordList').on('click', '.personDetail', function() {
				var str = {
					userName: $(this).attr("data-name"),
					userType: 1
				};
				var userDetail = {
					type: 'post',
					data: str,
					url: '/memberWithdraw/withdrawUserInfo',
					success: function(data) {
						_this.personDetail(data);
					}
				};
				base.sendRequest(userDetail);
			});
			//会员详情弹出更多
			$('#personDetail').on('click', '.getMoreMsg', function() {
				$('#personDetail .hide').removeClass('hide');
				$(this).addClass('hide');
			});
			//复制
			$("body").on('click', '.copyBtn', function() {
				_this.copyText($(this).attr('data-text'));
			});
		},
		//复制
		copyText: function(copyContent) {
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
		//		处理申请
		dealApplication: function(id) {
			var that = this;
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '代理处理申请',
					area: '52%',
					content: $('#dealApplication'),
					btn: ['查看打码量', '关闭', '保存'],
					yes: function() {
						parent.test4();
					},
					btn2: function() {},
					btn3: function() {
						if(that.dealApplication_status==2){
							layer.confirm('该会员状态为禁用，是否继续？', {
							  btn: ['继续','取消'] //按钮
							}, function(){
								$(this).addClass('layui-btn');
								var str = {
									id: id,
									state: parseInt(that.getVal($('#changeState'))),
									remark: $('#dealApplication .info').val() ? $('#dealApplication .info').val() : ''
								};
								if(str.remark.length > 50) {
									that.tipsContent('备注过长，请重新输入(小于50)', 500)
								} else {
									var sendChangeState = {
										type: 'post',
										data: str,
										url: '/memberWithdraw/memberDrawingManage',
										success: function success(data) {
											layer.closeAll('page');
											if(data.code == 200) {
												layer.closeAll('page');
												that.tipsContent('处理完成', 1000);
												that.loadDataList(that.prevStr);
		
											} else {
												that.tipsContent(data.msg, 1000);
											}
										}
									};
									base.sendRequest(sendChangeState);
								}
							
							}, function(){
								return;
							});
						}else{
							$(this).addClass('layui-btn');
							var str = {
								id: id,
								state: parseInt(that.getVal($('#changeState'))),
								remark: $('#dealApplication .info').val() ? $('#dealApplication .info').val() : ''
							};
							if(str.remark.length > 50) {
								that.tipsContent('备注过长，请重新输入(小于50)', 500)
							} else {
								var sendChangeState = {
									type: 'post',
									data: str,
									url: '/memberWithdraw/memberDrawingManage',
									success: function success(data) {
										layer.closeAll('page');
										if(data.code == 200) {
											layer.closeAll('page');
											that.tipsContent('处理完成', 1000);
											that.loadDataList(that.prevStr);
	
										} else {
											that.tipsContent(data.msg, 1000);
										}
									}
								};
								base.sendRequest(sendChangeState);
							}
						}
						return false;
					}
				});
			});
		},
		//会员详情展示
		personDetail: function(list) {
			var html = "";
			if(list.body) {
				var obj = list.body;
				html += '<table class="layui-table"><colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup><tbody> <td style="text-align: right;">\u767B\u5F55\u5E10\u53F7:</td><td style="text-align: left;">' + (obj.userName ? obj.userName : '-') + '</td><td style="text-align: right;">\u6240\u5C5E\u4E0A\u7EA7:</td><td style="text-align: left;">' + (obj.parentName ? obj.parentName : '-') + '</td></tr><tr><td style="text-align: right;">代理姓名:</td><td style="text-align: left;">' + (obj.fullName ? obj.fullName : '-') + '</td><td style="text-align: right;">\u72B6\u6001:</td><td style="text-align: left;">' + (obj.status ? obj.status == 2 ? '冻结' : '正常' : '-') + '</td></tr><tr><td style="text-align: right;">银行帐号:</td><td colspan="3" style="text-align: left;"><span>' + (obj.bankAccount ? obj.bankAccount : '-') + '</span><span data-text="' + obj.bankAccount + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span></td></tr><tr class="odd_row"><td style="text-align: right;">银行名称:</td><td style="text-align: left;">' + (obj.bankName ? obj.bankName : '-') + '</td><td style="text-align: right;">开户分行:</td><td style="text-align: left;">' + (obj.bankAddress ? obj.bankAddress : '-') + '</td></tr><tr class="getMoreMsg" rowspan="2" style="background:#666;color:#fff;cursor:pointer"><td colspan="4"><span>\u5C55\u5F00\u663E\u793A\u66F4\u591A<span></td></tr><tr class="hide"><td style="text-align: right;">代理余额:</td><td style="text-align: left;">' + (obj.coin ? obj.coin : '0') + '</td><td style="text-align: right;">手机号:</td><td style="text-align: left;">' + (obj.phoneNumber ? obj.phoneNumber : '-') + '</td></tr><tr class="odd_row hide"><td style="text-align: right;">LINE:</td><td style="text-align: left;">' + (obj.line? obj.line: '-') + '</td><td style="text-align: right;">\u90AE\u7BB1:</td><td style="text-align: left;">' + (obj.email ? obj.email : '-') + '</td></tr><tr><td style="text-align: right;">\u6CE8\u518CIP:</td><td style="text-align: left;">' + (obj.regIp ? obj.regIp : '-') + '</td><td style="text-align: right;">\u6CE8\u518C\u6765\u6E90:</td><td style="text-align: left;">' + (obj.channel ? (obj.channel==1?"pc":(obj.channel==2?"wap":(obj.channel==3?"app":" 管理员添加"))) : '-') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u6CE8\u518C\u7CFB\u7EDF:</td><td style="text-align: left;">' + (obj.regSystem ? obj.regSystem : '-') + '</td><td style="text-align: right;">\u6700\u540E\u767B\u5F55\u65F6\u95F4:</td><td style="text-align: left;">' + (obj.lastLoginTime ? obj.lastLoginTime : '-') + '</td></tr><tr><td style="text-align: right;">\u5907\u6CE8:</td><td colspan="3" style="text-align: left;">' + (obj.info ? obj.info : '-') + '</td></tr> </tbody></table>';
			} else {
				html = '<p>\u627E\u4E0D\u5230\u8BE5\u4F1A\u5458\u4FE1\u606F</p>';
			}

			$('#personDetail').html(html);
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '代理详情',
					area: ['44%', '37%'],
					content: $('#personDetail'),
					btn: ['确定'],
					yes: function yes(index, layero) {
						layer.closeAll('page');
					}
				});
			});
		},
		// 检测时间的先后顺序
		checkdate: function(start, end) {
			//得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
			//再进行判断就好判断了
			var sDate = new Date(start.replace(/\-/g, "\/"));
			var eDate = new Date(end.replace(/\-/g, "\/"));
			if(sDate > eDate) {
				layer.alert('结束时间不能早于开始时间', {
					skin: 'layui-layer-molv',
					closeBtn: 0,
					anim: 4 //动画类型
				});
				return false;
			}
			return true;
		}
	},
	watch: {
		pageNum: function() {
			this.selectPageNum();
		},
	},
});
$(function() {
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
			withdrawals.num = num;
			var str = {
				"pageIndex": withdrawals.num,
				"pageNum": parseInt(withdrawals.pageNum),
				"outOfThrity":parseInt(withdrawals.outOfThrity),
				"pageSize": 10,
			};
			if(withdrawals.prevStr == "") {
				withdrawals.prevStr = str;
			} else {
				withdrawals.prevStr.pageIndex = num;
				str = withdrawals.prevStr;
			}
			withdrawals.loadDataList(str);
		}
	});
});