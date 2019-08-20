let app = new Vue({
    el: "#app",
    data: {
        data: '',// 保存查询到的会员信息
        USER_NAME: '',// 保存参数 会员名称
        startTime: '',//开始时间
        endTime: '',//结束时间
    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.get_time();       //加载开始/结束日期控件
            this.queryUserDataSurvey();
        })
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
        // 查询会员信息
        queryUserDataSurvey: function () {
            if(sessionStorage.gaikuang){
                this.USER_NAME = sessionStorage.gaikuang;
                sessionStorage.gaikuang = '';
            }
            this.startTime = $('#startDate').val();
            this.endTime = $('#endDate').val();
            let _this = this;
            let obj = {
                type: 'post',
                data: {
                    'USER_NAME': _this.USER_NAME.trim(),
                    'startTime': _this.startTime,
                    'endTime': _this.endTime  //结束时间
                },
                dataType: 'json',
                url: "/reportManage/queryUserDataSurvey",
                success: function (data) {
                    if (data.code == 200) {
                        _this.data = data.body;
                    }
                },
                error: function (msg) {
                    //console.log("网络繁忙，请稍后再试");
                }
            };
            base.sendRequest(obj);
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
                    $('.col>span').removeClass('b_red');
                }
            });
            laydate.render({
                elem: '#endDate', //指定元素,
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                max: 0,
                value: "",
                done: function (value, date, endDate) {
                    $('.col>span').removeClass('b_red');
                }
            })
        },
        //补0
        getzf: function (num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        },
        //点击今日执行
        now: function () {
            $('.col>span').removeClass('b_red');
            $('#now').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击昨日执行
        yes: function () {
            $('.col>span').removeClass('b_red');
            $('#yes').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击本周执行
        week: function () {
            $('.col>span').removeClass('b_red');
            $('#week').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(0),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上周执行
        lastWeek: function () {
            $('.col>span').removeClass('b_red');
            $('#lastWeek').addClass('b_red');
            var st = this.getDateTime(2),
                et = this.getDateTime(3);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击本月执行
        month: function () {
            $('.col>span').removeClass('b_red');
            $('#month').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(4),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上月执行
        lastMonth: function () {
            $('.col>span').removeClass('b_red');
            $('#lastMonth').addClass('b_red');
            var st = this.getDateTime(6);
            var et = this.getDateTime(7);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //重置
        restbtn: function () {
            this.username = '';
            this.startTime = '';
            this.endTime = '';
            $('.btns>button').removeClass('b_red');
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
        // 验证会员名称是否存在
        selectUserCountByUserName: function () {
            let _this = this;
            if (_this.USER_NAME.trim() == null || _this.USER_NAME.trim() == '') {
                layer.msg("请输入会员名");
                return false;
            }
            let obj = {
                type: 'post',
                data: {
                    'USER_NAME': _this.USER_NAME.trim()
                },
                dataType: 'json',
                url: "/reportManage/selectUserCountByUserName",
                success: function (data) {
                    if (data.code == 200) {
                        // 判断是否存在该会员名称的会员
                        if (data.body > 0) {
                            //会员存在   则重新调用查询方法
                            app.queryUserDataSurvey();
                        } else {
                            layer.msg("该会员不存在");
                        }
                    }
                },
                error: function (msg) {
                    //console.log("网络繁忙，请稍后再试");
                }
            };
            base.sendRequest(obj);
        },
    }
});