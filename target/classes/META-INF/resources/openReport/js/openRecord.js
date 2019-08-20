var app=new Vue({
    el:"#app",
    data:{
        //赛事ID
        match_id:'',
        // 赛事种类
        types:[],
        store_sportsType:'',
        statius: [{ name: '未处理', code: 0 }, { name: '已处理', code: 1 }],
        store_statius:'',
        total:0,
        pageNum:'50',
        data_list:[]
    },
    created:function () {
        this.$nextTick(function () {
            this.getlayer();
            this.getType(); //获取赛事种类
            this.get_time(); //加载开始/结束日期控件
            this.store_sportsType = this.types[1].codeID;
        })
    },
    methods:{
        // 搜索
        search:function () {
            this.getdatas(1);
        },
        // 获取赛事种类
        getType:function () {
            let _this = this;
            let obj = {
                type: 'post',
                data: {
                },
                dataType: 'json',
                url: '/openRecord/queryMatchType',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.types = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //初始化获取数据
        getdatas: function (num,num1) {
            if(num1){
                this.now();
                this.store_sportsType = this.types[1].codeID;
            }
            this.Authorization = localStorage.acessToken;
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                matchType:_this.store_sportsType,
                matchId:_this.match_id,   
                openState: _this.store_statius,
                startTime: $("#startDate").val(),
                endTime: $("#endDate").val()
            };
            //console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/openRecord/queryOpenRecord',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.total=data.body.total
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages, //返回总页数
                            currentPage: num
                        });
                        if(data.body.list){
                            _this.data_list=data.body.list;
                        }
                    }else{
                        _this.data_list=[];
                        $('#fenye').jqPaginator('option', {
                            totalPages: 1, //返回总页数
                            currentPage: num
                        });
                        _this.total =0;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //加载layer
        getlayer() {
            layui.use('layer', function () {
                let layer = layui.layer;
            })
        },
        //点击今日执行
        now: function () {
            $('.search a').removeClass('b_red');
            $('#now').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击昨日执行
        yes: function () {
            $('.search a').removeClass('b_red');
            $('#yes').addClass('b_red');
            var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(s1 + " " + "00:00:00");
            $("#endDate").val(s1 + " " + "23:59:59");
        },
        //点击本周执行
        week: function () {
            $('.search a').removeClass('b_red');
            $('#week').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(0),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上周执行
        lastWeek: function () {
            $('.search a').removeClass('b_red');
            $('#lastWeek').addClass('b_red');
            var st = this.getDateTime(2),
                et = this.getDateTime(3);
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击本月执行
        month: function () {
            $('.search a').removeClass('b_red');
            $('#month').addClass('b_red');
            var dateTime = new Date(),
                st = this.getDateTime(4),
                et = dateTime.getFullYear() + "-" + this.getzf(dateTime.getMonth() + 1) + "-" + this.getzf(dateTime.getDate());
            $("#startDate").val(st + " " + "00:00:00");
            $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上月执行
        lastMonth: function () {
            $('.search a').removeClass('b_red');
            $('#lastMonth').addClass('b_red');
            var st = this.getDateTime(6);
            var et = this.getDateTime(7);
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
                    $('.search a').removeClass('b_red');
                }
            });
            laydate.render({
                elem: '#endDate', //指定元素,
                format: 'yyyy-MM-dd HH:mm:ss',
                type: 'datetime',
                max: 0,
                value: "",
                done: function (value, date, endDate) {
                    $('.search a').removeClass('b_red');
                }
            })
        },
        pageNum: function () {
            this.getdatas(1);
        },
    },
    watch: {
        //监听下拉框的值(每页显示多少条数据)
        pageNum: function () {
            this.getdatas(1);
        },
    }
})

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

    onPageChange: function (num, type) {
        app.getdatas(num);
    }
});

