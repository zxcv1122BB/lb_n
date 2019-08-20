'use strict';
var memAccCha = new Vue({
	el: "#container",
	data: {
		num: 1,
		pageNum: 50,
		prevStr: "",
		outOfThrity:0,
	},
	created: function() {},
	mounted: function() {
		this.setDateTime();
	},
	methods: {
		//日期
		setDateTime: function () {
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
		//补0
		getzf: function getzf(num) {
			if(parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},
		//日期设置
		getDateTime: function getDateTime(index) {
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
		getVal: function(elem) {
			return elem.find("option:selected").val();
		},
		loadDataList: function(str) {
			str.userType = 1;
			if(sessionStorage.zhanghao) {
				var userName = JSON.parse(sessionStorage.zhanghao);
				str.userName = userName.username;
				$("#label_list input[name='userName']").val(str.userName);
				sessionStorage.zhanghao = "";
			}
			var load = {
				type: 'get',
				data: str,
				url: '/coinLog/queryCoinLogList',
				success: function(data) {
					var html = '';
					if(data.body.list == null || data.body.list == "") {
						$('#page').jqPaginator('option', {
							totalPages: 1,
							currentPage: 1
						});
						$('#paging_record .sumRecord').html(0);
						html += '<tr><td colspan="10">\u6682\u65E0\u8BB0\u5F55</td></tr>';
					} else {
						$('#page').jqPaginator('option', {
							totalPages: data.body.pages
						});
						$('#paging_record .sumRecord').html(data.body.total);
						if(data.body.list.length>1){
                            for(var i = 0; i < data.body.list.length; i++) {
                                var obj = data.body.list[i];
                                html += '<tr><td>' + (obj.id ? obj.id : "-") + '</td><td>' + (obj.userName ? obj.userName : "-") + '</td><td>' + (obj.CoinOperateName ? obj.CoinOperateName : "-") + '</td><td>' + (obj.coinBefore ? obj.coinBefore : "-") + '</td><td>' + (obj.coin ? obj.coin : "-") + '</td><td>' + (obj.userCoin ? obj.userCoin : "-") + '</td><td>' + (obj.inputTime ? obj.inputTime : "-") + '</td><td>' + (obj.orderId ? obj.orderId : "-") + '</td><td>' + (obj.operateUser ? obj.operateUser : "-") + '</td><td>' + (obj.info ? obj.info : "-") + '</td></tr>';
                            }

						}else{
							html = "<tr><td colspan='10'>暂无数据</td></tr>"

						}

					}

					$('#recordList').html(html);
				}
			};
			base.sendRequest(load);
		},
		btn_select_click: function() {
			var that = this,
				str = {
					"pageIndex": that.num,
					"pageNum": parseInt(that.pageNum),
					"pageSize": 10,
					"outOfThrity":parseInt(that.outOfThrity),
					"userName": '',
					"orderId": "",
					"agentCount": "",
					"startTime": "",
					"endTime": "",
					"coinOperateType": ""
				};
			str.coinOperateType = parseInt(that.getVal($("#allType"))) == 0 ? "" : parseInt(that.getVal($("#allType")));
			str.userName = $("#label_list input[name='userName']").val().trim() ? $("#label_list input[name='userName']").val().trim() : '';
			str.orderId = $("#label_list input[name='orderId']").val().trim() ? $("#label_list input[name='orderId']").val().trim() : '';
			str.agentCount = $("#label_list input[name='accountName']").val().trim() ? $("#label_list input[name='accountName']").val().trim() : '';
			str.startTime = $('#startDate').val();
			str.endTime = $('#endDate').val();
			str.userType = 1;
			that.prevStr = str;
			var select_byName = {
				type: 'get',
				url: '/coinLog/queryCoinLogList',
				data: str,
				dataType: 'json',
				success: function(data) {
					var html = '';
					if(data.body.list == null || data.body.list == "") {
						$('#page').jqPaginator('option', {
							totalPages: 1,
							currentPage: 1
						});
						$('#paging_record .sumRecord').html(0);
						html += '<tr><td colspan="10">\u6682\u65E0\u8BB0\u5F55</td></tr>';
					} else {
						$('#page').jqPaginator('option', {
							totalPages: data.body.pages
						});
						$('#paging_record .sumRecord').html(data.body.total);
						for(var i = 0; i < data.body.list.length; i++) {
							var obj = data.body.list[i];
							html += '<tr><td>' + (obj.id ? obj.id : "-") + '</td><td>' + (obj.userName ? obj.userName : "-") + '</td><td>' + (obj.CoinOperateName ? obj.CoinOperateName : "-") + '</td><td>' + (obj.coinBefore ? obj.coinBefore : "-") + '</td><td>' + (obj.coin ? obj.coin : "-") + '</td><td>' + (obj.userCoin ? obj.userCoin : "-") + '</td><td>' + (obj.inputTime ? obj.inputTime : "-") + '</td><td>' + (obj.orderId ? obj.orderId : "-") + '</td><td>' + (obj.operateUser ? obj.operateUser : "-") + '</td><td>' + (obj.info ? obj.info : "-") + '</td></tr>';
						}
					}
					$('#recordList').html(html);
				},
				error: function error(e) {
					//console.log(e);
				}
			};
			// 检测用户输入的时间是否符合规范（开始时间小于结束时间）
			if(that.checkdate(str.startTime, str.endTime)) {
				base.sendRequest(select_byName);
			}
		},
		//页面显示数据条数
		selectPageNum: function() {
			var that = this;
			$('#page').jqPaginator('option', {
				currentPage: 1
			});
			that.num = 1;
			that.prevStr.pageIndex = 1;
			that.prevStr.pageNum = parseInt(that.pageNum);
			that.loadDataList(that.prevStr);
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
			memAccCha.num = num;
			var str = {
				"pageIndex": memAccCha.num,
				"pageNum": parseInt(memAccCha.pageNum),
				"pageSize": 10,
				"outOfThrity":parseInt(memAccCha.outOfThrity),
			};
			if(memAccCha.prevStr == "") {
				memAccCha.prevStr = str;
			} else {
				memAccCha.prevStr.pageIndex = num;
				str = memAccCha.prevStr;
			}
			memAccCha.loadDataList(str);
		}
	});
});