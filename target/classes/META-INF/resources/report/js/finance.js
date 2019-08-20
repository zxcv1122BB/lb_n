//使用vue.js
var app = new Vue({
	el: '#app',
	data: {
		datas: [], //记录列表
		total: 0, //信息总条数
		pageNum: 50,
		
		username:'',//用户账号
		startDate: '',//开始时间
        endDate: '',//结束时间
        flag:false,   //当用户点击某一个日期区段选择按钮时执行
        selectStartTime:'',     //用户选择的时间区段的开始时间
        selecEndTime:'',        //用户选择的时间区段的结束时间
        outOfThrity: "0",  //时间
	},
	created: function() {
		//列表数据
		this.$nextTick(function () {
			this.month();
			this.getdatas();
        })
		
	},

	//利用ajax来查询记录列表
	methods: {
		//获取财务报表
		getdatas: function(num) {
			var _this = this;
			if(num==null){
				num=1;
            }
            if (_this.flag) {
                _this.flag = false;
                _this.startDate = _this.selectStartTime;
                _this.endDate = _this.selecEndTime;
            }
            //console.log(_this.startDate+"----"+_this.endDate)
			var obj = {
				type: 'get',
				data: {
					'pageIndex':num,
					'pageNum': parseInt(_this.pageNum),
					'pageSize': 10,
					'startTime':$('#startDate').val(),
					'endTime':$('#endDate').val(),
					'outOfThrity': this.outOfThrity,   //时间
					'userName':_this.username.trim()
                },
				dataType: 'json',
				url: '/reportManage/queryFinanceListPage',
				success: function(data) {
					if(data.code == 200) {
						//取到后台传递多来的Body下面的List
						_this.datas = data.body.list;
						_this.total = data.body.total;
						if(_this.datas.length>0){
							$("#fenye").jqPaginator("option",{
								totalPages: data.body.pages, //返回总页数
								currentPage: 1
							});
						}else{
							$("#fenye").jqPaginator("option",{
								totalPages: 1, //返回总页数
								currentPage: 1
							});
						}
					}
				},
				error: function(msg) {
					//console.log(msg);
				}
            };
            //console.log(obj.startDate)
            
            if (_this.startDate != undefined || _this.endDate!=undefined){
                // 检测用户输入的时间是否符合规范（开始时间小于结束时间）
                if (_this.checkdate(_this.startDate, _this.endDate)) {
                    base.sendRequest(obj);
                }
                return ;
            }
		    base.sendRequest(obj);
		},
      //点击今日执行
        now: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#now').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
            _this.selectStartTime = s2+ " " + "00:00:00";
            _this.selecEndTime = s2+ " " + "23:59:59";
            $("#startDate").val(s2 + " " + "00:00:00");
			$("#endDate").val(s2 + " " + "23:59:59")
        },
        //点击昨日执行
        yes: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#yes').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
            _this.selectStartTime = s1 +" " + "00:00:00";
            _this.selecEndTime = s1+ " " + "23:59:59";
            $("#startDate").val(s1 + " " + "00:00:00");
			$("#endDate").val(s1 + " " + "23:59:59")
        },
        //点击本周执行
        week: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#week').addClass('b_red');
            var dateTime = new Date(),
                st = _this.getDateTime(0),
                et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
            _this.selectStartTime = st +" " + "00:00:00";
            _this.selecEndTime = et + " " + "23:59:59";
            $("#startDate").val(st + " " + "00:00:00");
			$("#endDate").val(et + " " + "23:59:59")
        },
        //点击上周执行
        lastWeek: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#lastWeek').addClass('b_red');
            var st = _this.getDateTime(2),
                et = _this.getDateTime(3);
            _this.selectStartTime = st+ " " + "00:00:00";
            _this.selecEndTime = et+ " " + "23:59:59";
            $("#startDate").val(st + " " + "00:00:00");
			$("#endDate").val(et + " " + "23:59:59")
        },
        //点击本月执行
        month: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#month').addClass('b_red');
            var dateTime = new Date(),
                st = _this.getDateTime(4),
                et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
            _this.selectStartTime = st+ " " + "00:00:00";
            _this.selecEndTime = et+ " " + "23:59:59";
            $("#startDate").val(st + " " + "00:00:00");
			$("#endDate").val(et + " " + "23:59:59")
			
        },
        //点击上月执行
        lastMonth: function () {
        	var _this = this;
            $('.btns>button').removeClass('b_red');
            $('#lastMonth').addClass('b_red');
            var st = _this.getDateTime(6);
            var et = _this.getDateTime(7);
            _this.selectStartTime = st+ " " + "00:00:00";
            _this.selecEndTime = et+ " " + "23:59:59";
            $("#startDate").val(st + " " + "00:00:00");
			$("#endDate").val(et + " " + "23:59:59")
        },
        //日期设置
        getDateTime: function (index) {
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
                if (mymonth < 10) {
                    mymonth = "0" + mymonth;
                }
                if (myweekday < 10) {
                    myweekday = "0" + myweekday;
                }
                return (myyear + "-" + mymonth + "-" + myweekday);
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
                if (nowMonth < 3) {
                    quarterStartMonth = 0;
                }
                if (2 < nowMonth && nowMonth < 6) {
                    quarterStartMonth = 3;
                }
                if (5 < nowMonth && nowMonth < 9) {
                    quarterStartMonth = 6;
                }
                if (nowMonth > 8) {
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
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + 1 + (6 - nowDayOfWeek));
                return formatDate(weekEndDate);
            }

            //获得上周的开始日期
            function getLastWeekStartDate() {
                var weekStartDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek - 7);
                return formatDate(weekStartDate);
            }

            //获得上周的结束日期
            function getLastWeekEndDate() {
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + 1 - nowDayOfWeek - 1);
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

            if (index == 0) {
                var k = getWeekStartDate();
                return k
            } else if (index == 1) {
                var k = getWeekEndDate();
                return k
            } else if (index == 2) {
                var k = getLastWeekStartDate();
                return k
            } else if (index == 3) {
                var k = getLastWeekEndDate();
                return k
            } else if (index == 4) {
                var k = getMonthStartDate();
                return k
            } else if (index == 5) {
                var k = getMonthEndDate();
                return k
            } else if (index == 6) {
                var k = getLastMonthStartDate();
                return k
            } else if (index == 7) {
                var k = getLastMonthEndDate();
                return k
            }
        },
        //补0
        getzf: function (num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        },
        //点击搜索框执行
        search: function () {
            this.flag=true;
        	this.getdatas(1);
        },
        //重置
        restbtn:function(){
        	var _this = this;
        	_this.username = '';
        	$('#startDate').val('');
            $('#endDate').val('');
        	$('.btns>button').removeClass('b_red');
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
	//加载选择开始/结束日期控件
	mounted: function () {
		var _this = this;
		
        //日期设置
        laydate.render({
            elem: '#startDate', //指定元素
            format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime',   
				max: 0,  
				value: "",
                done: function(value, date, endDate){
                    $('.layui-tab span.b_red').removeClass('b_red');
                    $('.btns>button').removeClass('b_red');
                     _this.selectStartTime = value
                    _this.startDate=value;
                }
        });
        laydate.render({
            elem: '#endDate', //指定元素
            format: 'yyyy-MM-dd 23:59:59',
				type: 'datetime',
				max: 0,
    			value: "",
                done: function(value, date, endDate){
                    $('.layui-tab span.b_red').removeClass('b_red');
                    $('.btns>button').removeClass('b_red');
                    _this.endDate=value;
                    _this.selecEndTime = value
                    }
        });
    },
	watch:{
		//监听下拉框的值(每页显示多少条数据)
		pageNum: function() {
			this.getdatas(1);
		},
	}
});

//加载分页功能
$.jqPaginator('#fenye', {
	totalPages: 1, //多少页数据
	visiblePages: 5, //最多显示几页
	currentPage: 1, //当前页
	wrapper: '<ul class="pagination"></ul>',
	first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
	prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
	next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
	last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
	page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
	onPageChange: function(num) {
		app.getdatas(num);
	}
});