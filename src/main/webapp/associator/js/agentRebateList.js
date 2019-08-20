/**
 * Created by ASUS on 2017/9/23.
 */
var app = new Vue({
	el: "#app",
	data: {
		datas: [],//运营分析
		Agencys: [],
		page_num: 50,//每页显示多少条
		prevData: '',
		num1:0,
        total:'',	//总条数
        startDate:'', //开始时间
        endDate: '',  //结束时间
        // loginAccount: '', //代理账号
        outOfThrity: '0',
        proxyName:'',
        Rebackdata:[]
	},
	created: function() {
		this.$nextTick(function () {
            this.setDataTime();
            this.getDateTime();
        })
	},
	mounted:function(){
		this.click();
	},
	methods: {
        proxy_rebate:function(datas){
            var _this = this, datas = datas.replace("ssc","时时彩").replace("k3","快3").replace("11x5","11选5").replace("3D","福彩3D").replace("PK10","PK10").replace('hk6','六合彩').replace('7xc','七星彩').replace('kl10f','快乐十分').replace('PCDD','北京28');
            _this.Rebackdata = datas.split("@");
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title:'查看返点数',
                    area: ['200px', '300px'], //宽高
                    content: $('.rebackRebeta')
                });
            })

        },

		//日期设置
		getDateTime: function(index) {
			var now = new Date(); //当前日期
			var nowDayOfWeek = now.getDay()||7; //今天本周的第几天
			var nowDay = now.getDate(); //当前日
			var nowMonth = now.getMonth(); //当前月
			var nowYear = now.getYear(); //当前年
			nowYear += (nowYear < 2000) ? 1900 : 0; //
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
				return(myyear + "-" + mymonth + "-" + myweekday);
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
				var quarterStartDate = new Date(nowYear, quarterEndMonth,
					getMonthDays(quarterEndMonth));
				return formatDate(quarterStartDate);
			}

			if(index == 0) {
				var k = getWeekStartDate();
				return k
			} else if(index == 1) {
				var k = getWeekEndDate();
				return k
			} else if(index == 2) {
				var k = getLastWeekStartDate();
				return k
			} else if(index == 3) {
				var k = getLastWeekEndDate();
				return k
			} else if(index == 4) {
				var k = getMonthStartDate();
				return k
			} else if(index == 5) {
				var k = getMonthEndDate();
				return k
			} else if(index == 6) {
				var k = getLastMonthStartDate();
				return k
			} else if(index == 7) {
				var k = getLastMonthEndDate();
				return k
			}
		},
		//补0
		getzf: function(num) {
			if(parseInt(num) < 10) {
				num = '0' + num;
			}
			return num;
		},


		//日期选择器设置
		setDataTime: function(max) {
        	if(!max){
        		max=0
			}
            layui.use('laydate', function(){
                var laydate = layui.laydate;
                //	日期设置
                laydate.render({
                    elem: '#startDate', //指定元素
                    format: 'yyyy-MM-dd HH:mm:ss',
                    type: 'datetime',
                    max: max,
                    value: "",
                    done: function(value, date, endDate){
                        $('.layui-tab span.b_red').removeClass('b_red');
                    }
                });
                laydate.render({
                    elem: '#endDate', //指定元素
                    format: 'yyyy-MM-dd 23:59:59',
                    type: 'datetime',
                    max: max,
                    value: "",
                    done: function(value, date, endDate){
                        $('.layui-tab span.b_red').removeClass('b_red');
                    }

                });
            });


		},
		click: function() {
			var _this = this;
			//按钮-----今天
			$('#now').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var dateTime = new Date();
				dateTime.setTime(dateTime.getTime());
				var s2 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
				$("#startDate").val(s2 + " " + "00:00:00");
				$("#endDate").val(s2 + " " + "23:59:59");
			});
			//按钮-----昨天
			$('#yes').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var dateTime = new Date();
				dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
				var s1 = dateTime.getFullYear() +
					"-" + _this.getzf(dateTime.getMonth() + 1) +
					"-" + _this.getzf(dateTime.getDate());
				$("#startDate").val(s1 + " " + "00:00:00");
				$("#endDate").val(s1 + " " + "23:59:59");

			});
			//按钮-----本周
			$('#week').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var dateTime = new Date(),
					st = _this.getDateTime(0),
					et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate())
				$("#startDate").val(st + " " + "00:00:00");
				$("#endDate").val(et + " " + "23:59:59");
			});
			//按钮-----上周
			$('#lastWeek').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var st = _this.getDateTime(2),
					et = _this.getDateTime(3);
				$("#startDate").val(st + " " + "00:00:00");
				$("#endDate").val(et + " " + "23:59:59");
			});
			//按钮-----本月
			$('#month').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var dateTime = new Date(),
					st = _this.getDateTime(4),
					et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
				$("#startDate").val(st + " " + "00:00:00");
				$("#endDate").val(et + " " + "23:59:59");

			});
			//按钮-----上月
			$('#lastMonth').on('click', function() {
				$('.layui-tab span.b_red').removeClass('b_red');
				$(this).addClass('b_red');
				var st = _this.getDateTime(6);
				var et = _this.getDateTime(7);
				$("#startDate").val(st + " " + "00:00:00");
				$("#endDate").val(et + " " + "23:59:59");
			});
		},
		//获取用户信息列表
		getOperation: function(num) {
			var _this = this;
			var data = {
				pageIndex: num,
				pageNum: parseInt(_this.page_num),
				pageSize: 10,
				startDate:_this.startDate, //开始时间
				endDate: _this.endDate,  //结束时间
				loginAccount: _this.proxyName, //代理账号
                outOfThrity:_this.outOfThrity, //7天类型
			};
			//console.log(data);
			var options = {
				type: "get",
				url: "/reportManage/agentRebateListList",
				dataType: "json",
				data: data,
				success: function(data) {
					//console.log(data);
					if(data.code==200){
						_this.datas = data.body.list;
						_this.total = data.body.total;
						if(data.body.total===0){
							$('#fenye').jqPaginator('option', {
								totalPages: 1,
								currentPage: 1
							});
						}else{
							$('#fenye').jqPaginator('option', {
								totalPages: data.body.pages,
								currentPage: 1
							});
						}
					}
				},
				error: function(msg) {
					//console.log(msg)
				}
			};
			if (data.startDate != undefined || data.endDate != undefined) {
				// 检测用户输入的时间是否符合规范（开始时间小于结束时间）
				if (_this.checkdate(data.startDate, data.endDate)) {
					base.sendRequest(options);
				}
				return;
			}
			base.sendRequest(options);
		},
//		点击回滚操作
		rollBack:function(id,agentId,rebatesAmount){
			var _this = this;
			var data = {
				id:id,
				agentId:agentId,
				rebatesAmount:rebatesAmount
			};
			//console.log(data);
			var options = {
					type: "get",
					url: "/reportManage/rollBack",
					dataType: "json",
					data: data,
					success: function(data) {
						//console.log(data);
						if(data.code==200){
                            layer.msg(data.msg);
                            setTimeout(function () {
                                window.location.reload();
                            }, 1000);
						}else if(data.code==501){
							layer.msg(data.msg);
						}
					},
					error: function(msg) {
						//console.log(msg)
					}
				};
				base.sendRequest(options);
		},
		// 检测时间的先后顺序
		checkdate: function (start, end) {
			//得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
			//再进行判断就好判断了
			var sDate = new Date(start.replace(/\-/g, "\/"));
			var eDate = new Date(end.replace(/\-/g, "\/"));
			if (sDate > eDate) {
				layer.alert('结束时间不能早于开始时间', {
					skin: 'layui-layer-molv'
					, closeBtn: 0
					, anim: 4 //动画类型
				});
				return false;
			}
			return true;
		}
	},
	watch: {
		page_num: function() {
			this.getOperation(1);
		},
    }


});

	// 加载分页功能
	$.jqPaginator('#fenye', {
		totalPages: 1, //多少页数据
		visiblePages: 10, //最多显示几页
		currentPage: 1, //当前页
		wrapper: '<ul class="pagination"></ul>',
		first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
		onPageChange: function(num, type) {
			app.getOperation(num);
		}
	});
