$(function(){
	$('.btns a').click(function(){ 
		$(this).addClass('active');
		$(this).siblings('a').removeClass('active');
	})
})
let app = new Vue({
    el: '#app',
    data: {
        datas: [],   //展示数据
        total: '',    //总条数-绑定
        //下面参数是搜索时要传的:
        username: '',         //会员帐号
        agentCount: '',       //代理及下线
        tipsLoad:''

    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.get_time();       //加载开始/结束日期控件
            this.month();
            this.getdatas();
        })
    },
    methods: {
        //加载layer
        getlayer(){
            layui.use('layer',function () {
                let layer = layui.layer;
            })
        },
        //加载选择开始/结束日期控件
        get_time: function () {
            //	日期设置
            laydate.render({
                elem: '#startDate', //指定元素
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                max: 0,
                value: "",
                done: function (value, date, endDate) {
                    $('.search .b_red').removeClass('b_red');
                }
            });
            laydate.render({
                elem: '#endDate', //指定元素
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                max: 0,
                value: "",
                done: function (value, date, endDate) {
                    $('.search .b_red').removeClass('b_red');
                }
            })
        },
        //初始化获取数据
        getdatas: function () {
        	let _this = this;
            let data = {
        		agentCount: _this.agentCount.trim(),
                username: _this.username.trim(),
                startTime:$("#startDate").val(),
                endTime:$("#endDate").val()
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/reportManage/globalCount',
                success: function (data) {
                    if (data.code == 200) {
                        var i="",it;
                        for(i in data.body){
                            it=parseFloat(data.body[i]);
                            if(!isNaN(it)){
                                if(it>0){
                                    data.body[i+"_color"]="color:red;"
                                }else if(it<0){
                                    data.body[i+"_color"]="color:blue;";
                                }else{
                                    data.body[i+"_color"]="";
                                }
                            }else{
                                data.body[i+"_color"]="";
                            }
                        }
                        _this.datas = data.body;
                        
                    } else {
                        layer.msg(data.msg)
                    }
                },
                error: function (msg) {
                }
            };
            base.sendRequest(obj);
        },
        //重置按钮
        reset: function () {
        	let _this = this;
        	_this.agentCount="";
        	_this.username = "";
        	$(".search>.col_1>.btns>a").removeClass('active');
        	$("#now").addClass('active');
        	_this.now();
        },
        //点击今日执行
        now: function () {
        	let _this = this;
        	var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) 
            	+ "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击昨日执行
        yes: function () {
        	let _this = this;
        	var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s1 + " " + "00:00:00");
            $("#endDate").val(s1 + " " + "23:59:59");
        },
        //点击本周执行
        week: function () {
        	let _this = this;
        	var dateTime = new Date(),
            st = this.getDateTime(0),
            et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上周执行
        lastWeek: function () {
        	let _this = this;
        	var st = _this.getDateTime(2),
            et = _this.getDateTime(3);
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击本月执行
        month: function () {
        	let _this = this;
        	var dateTime = new Date(),
            st = _this.getDateTime(4),
            et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上月执行
        lastMonth: function () {
        	let _this = this;
        	var st = _this.getDateTime(6);
            var et = _this.getDateTime(7);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
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
                ////console.log(nowYear, nowMonth, nowDay - nowDayOfWeek);
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
            this.tipsLoad=layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
            var _this = this;
            var obj = {
                type: 'get',
                data: {
                	agentCount: _this.agentCount.trim(),
                    username: _this.username.trim(),
                    startTime:$("#startDate").val(),
                    endTime:$("#endDate").val()
                },
                dataType: "json",
                url: '/reportManage/globalCount',
                success: function (data) {
                    if(_this.tipsLoad){
                            layer.closeAll();
                            _this.tipsLoad="";
                    }
                    if (data.code == 200) {
                        this.datas = [];
                        var i="",it;
                        for(i in data.body){
                            it=parseFloat(data.body[i]);
                            if(!isNaN(it)){
                                if(it>0){
                                    data.body[i+"_color"]="color:red;"
                                }else if(it<0){
                                    data.body[i+"_color"]="color:blue;";
                                }else{
                                    data.body[i+"_color"]="";
                                }
                            }else{
                                data.body[i+"_color"]="";
                            }
                        }
                        _this.datas = data.body;
                    } else {
                        layer.alert(data.msg, {
                            skin: 'layui-layer-molv'
                            , closeBtn: 0
                            , anim: 4 //动画类型
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            setTimeout(function(){
                base.sendRequest(obj);
            },101);
                
            
        },
    },
    watch: {
        agentCount: function (val) {
            //console.log(val.toString().substring(0, val.toString().length - 1))
            if (val.toString().length - 1 == '') {
                //console.log('-------');
                // val=val.toString().length=val.toString().length-1;
                //console.log(val.toString().length);
                // this.agentCount=val
            }
        }
    }
});

