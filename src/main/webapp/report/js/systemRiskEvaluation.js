/**
 * Created by ASUS on 2017/9/23.
 */
let app = new Vue({
    el: "#app",
    data: {
        datas: [],//运营分析
        yy_totalled:{},//运营总计
        username_proxy: '',//绑定会员帐号或代理
        getType: 0,         //默认查会员
        page_num: 50,	   //默认显示条数
        total: '',		   //总条数
        type: 1,    //1为运营分析，2为彩票分析
        datasLottery: [],//彩票分析
        cp_totalled:{},//彩票总计
        outOfThrity: "0",  //时间

        caiAllType:"1,2,3",
        caiType:'',
        caiTypeList:[]
    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.setDataTime();
            this.getDateTime();
        })
    },
    mounted: function () {
        this.click();
        $('#month').trigger('click');
        this.getCaiTypeList();
    },
    methods: {
        getCaiTypeList(){
            
            var _this = this;
            var options = {
                type: "get",
                url: "/bets/queryGameType",
                dataType: "json",
                data: {
                    type:_this.caiAllType
                },
                success: function (data) {
                    if(data.code==200){
                        _this.caiTypeList=data.body;
                    }else{
                        _this.caiTypeList=[];
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                    _this.caiTypeList=[];
                }
            };
            base.sendRequest(options);
        },
        //加载layer、element
        getlayer() {
            layui.use(["layer", 'element'], function () {
                let layer = layui.layer,
                    element = layui.element;
            });
        },
        //获取运营分析列表
        getDeposit: function (num) {
            let startDate = $('#startDate').val();
            let endDate = $('#endDate').val();
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 10,
                startDate: startDate,
                endDate: endDate,
                userType: this.getType == 0 ? '' : this.getType,
                outOfThrity: this.outOfThrity,   //时间
                userName: this.username_proxy
            };
            let _this = this;
            let options = {
                type: "get",
                url: "/coinLog/getDepositList",
                dataType: "json",
                data: data,
                success: function (data) {
                    ////console.log(data);
                    _this.datas = data.body.pageInfo.list;
                    _this.total = data.body.pageInfo.total;
                    _this.yy_totalled = data.body.coinLogMap;
                    if (data.body.pageInfo.list.length > 0) {
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pageInfo.pages,
                            currentPage: 1
                        });
                    } else {
                        $('#fenye').jqPaginator('option', {
                            totalPages: 1,
                            currentPage: 1
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //获取彩票分析列表
        getLottery: function (num) {
            let startDate = $('#startDate').val();
            let endDate = $('#endDate').val();
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 10,
                startDate: startDate,
                endDate: endDate,
                userType: 2,
                outOfThrity: this.outOfThrity,   //时间
                userName: this.username_proxy,

                gameType:  this.caiAllType=="1,2,3"?1:this.caiAllType=="1,2"?2:3,
                
                gameID:this.caiType
            };
            ////console.log(data);
            let _this = this;
            let options = {
                type: "get",
                url: "/reportManage/getLotteryList",
                dataType: "json",
                data: data,
                success: function (data) {
                    ////console.log(data);
                    _this.datasLottery = data.pageInfo.list;
                    _this.total = data.pageInfo.total;
                    _this.cp_totalled = data.gameBetMap;
                    if (data.pageInfo.list.length > 0) {
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.pageInfo.pages,
                            currentPage: 1
                        });
                    } else {
                        $('#fenye').jqPaginator('option', {
                            totalPages: 1,
                            currentPage: 1
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //点击运营分析
        change1: function () {
            this.type = 1;
            this.getDeposit(1);
        },
        //点击彩票分析
        change2: function () {
            this.type = 2;
            this.getLottery(1);
        },
        //点击查询
        search: function () {
            if (this.type == 1) {
                this.getDeposit(1);
            } else {
                this.getLottery(1);
            }
        },
        resetting(){
            this.outOfThrity= "0";
            this.username_proxy='';
            this.getType=0;
            $(".quitTime .b_red").removeClass('b_red');
            $('#startDate').val('');
            $('#endDate').val('');
	          /* this.agent_store='';
	           this.dateType='';  //今日
	           this.type=1;
	           this.uid='';
	           this.username = '';
           	   $('.col span').removeClass("active");
	   		   $('.col span:first-child').addClass("active");
	   		   $('#username').val('');
	   		   $('.col box1').val('');
			if (this.type == 1){
				this.getDeposit(1);
			}else{
				this.getLottery(1);
			}*/
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
        //日期选择器设置
        setDataTime: function () {
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
            });
        },
        //初始化点击事件
        click: function () {
            let _this = this;
            //按钮-----今天
            $('#now').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#now').addClass('b_red');
                let dateTime = new Date();
                dateTime.setTime(dateTime.getTime());
                let s2 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
                _this.selectStartDate = s2 + " " + "00:00:00";
                _this.selectEndDate = s2 + " " + "23:59:59";
                $("#startDate").val(s2 + " " + "00:00:00");
                $("#endDate").val(s2 + " " + "23:59:59");
            });
            //按钮-----昨天
            $('#yes').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#yes').addClass('b_red');
                let dateTime = new Date();
                dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
                let s1 = dateTime.getFullYear() +
                    "-" + _this.getzf(dateTime.getMonth() + 1) +
                    "-" + _this.getzf(dateTime.getDate());
                _this.selectStartDate = s1 + " " + "00:00:00";
                _this.selectEndDate = s1 + " " + "23:59:59";
                $("#startDate").val(s1 + " " + "00:00:00");
                $("#endDate").val(s1 + " " + "23:59:59");

            });
            //按钮-----本周
            $('#week').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#week').addClass('b_red');
                let dateTime = new Date(),
                    st = _this.getDateTime(0),
                    et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate())
                _this.selectStartDate = st + " " + "00:00:00";
                _this.selectEndDate = et + " " + "23:59:59";
                $("#startDate").val(st + " " + "00:00:00");
                $("#endDate").val(et + " " + "23:59:59");
            });
            //按钮-----上周
            $('#lastWeek').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#lastWeek').addClass('b_red');
                let st = _this.getDateTime(2),
                    et = _this.getDateTime(3);
                $("#startDate").val(st + " " + "00:00:00");
                $("#endDate").val(et + " " + "23:59:59");
                _this.selectStartDate = st + " " + "00:00:00";
                _this.selectEndDate = et + " " + "23:59:59";
            });
            //按钮-----本月
            $('#month').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#month').addClass('b_red');
                let dateTime = new Date(),
                    st = _this.getDateTime(4),
                    et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
                $("#startDate").val(st + " " + "00:00:00");
                $("#endDate").val(et + " " + "23:59:59");
                _this.selectStartDate = st + " " + "00:00:00";
                _this.selectEndDate = et + " " + "23:59:59";

            });
            //按钮-----上月
            $('#lastMonth').on('click', function () {
                $('.col span').removeClass('b_red');
                $('#lastMonth').addClass('b_red');
                let st = _this.getDateTime(6);
                let et = _this.getDateTime(7);
                $("#startDate").val(st + " " + "00:00:00");
                $("#endDate").val(et + " " + "23:59:59");
                _this.selectStartDate = st + " " + "00:00:00";
                _this.selectEndDate = et + " " + "23:59:59";
            });
        },
        inline: function () {
            app.getOperation(1);
            app.getDeposit(1);
            app.getLottery(1);
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
        page_num: function () {
            if (this.type == 1) {
                this.getDeposit(1);
            } else {
                this.getLottery(1);
            }
        },
        caiAllType:function(){
            // this.getCaiTypeList()
        }
    },
    computed: {
        //运营分析小计：
        dp_count: function () {  //存款次数
            let num = 0;
            for (let i = 0; i < this.datas.length; i++) {
                if (this.datas[i].depositCount != undefined) {
                    num = num + this.datas[i].depositCount;
                }
            }
            return num;
        },
        wd_count: function () {  //取款次数
            let num = 0;
            for (let i = 0; i < this.datas.length; i++) {
                if (this.datas[i].withdrawalCount != undefined) {
                    num = num + this.datas[i].withdrawalCount;
                }
            }
            return num;
        },
        dp_account: function () {    //存款额
            let num = 0;
            for (let i = 0; i < this.datas.length; i++) {
                if (this.datas[i].depositAccount != undefined) {
                    num = num + parseFloat(this.datas[i].depositAccount);
                }
            }
            return parseFloat(num).toFixed(2);
        },
        wd_account: function () {    //取款额
            let num = 0;
            for (let i = 0; i < this.datas.length; i++) {
                if (this.datas[i].withdrawalAcount != undefined) {
                    num = num + parseFloat(this.datas[i].withdrawalAcount);
                }
            }
            return parseFloat(num).toFixed(2);
        },
        bl_account: function () {    //盈亏额
        	
            let num = 0;
            for (let i = 0; i < this.datas.length; i++) {
                if (this.datas[i].balance != undefined) {
                    num = num + parseFloat(this.datas[i].balance);
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //彩票分析小计:
        tze: function () {   //彩票投注额
            let num = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].totalHandleAccount != undefined) {
                    num = num + this.datasLottery[i].totalHandleAccount;
                }
            }
            return num.toFixed(2);
        },
        zje: function () {   //彩票中奖额
            let num = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].lotteryAccount != undefined) {
                    num = num + this.datasLottery[i].lotteryAccount;
                }
            }
            return num.toFixed(2);
        },
        yke: function () {   //彩票盈亏额
            let num = 0;
            let num2 = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].totalHandleAccount != undefined) {
                    num = num + this.datasLottery[i].totalHandleAccount;
                }
                if (this.datasLottery[i].lotteryAccount != undefined) {
                    num2 = num2 + this.datasLottery[i].lotteryAccount;
                }
            }
            return (num2 - num).toFixed(2);
        },
        tz_count: function () {  //彩票投注次数
            let num = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].totalHandleCount != undefined) {
                    num = num + this.datasLottery[i].totalHandleCount;
                }
            }
            return num;
        },
        zj_count: function () {  //彩票中奖次数
            let num = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].lotteryCount != undefined) {
                    num = num + this.datasLottery[i].lotteryCount;
                }
            }
            return num;
        },
        zjl: function () {   //彩票中奖率
            let num = 0;
            let num2 = 0;
            for (let i = 0; i < this.datasLottery.length; i++) {
                if (this.datasLottery[i].totalHandleCount != undefined) {
                    num = num + this.datasLottery[i].totalHandleCount;
                }
                if (this.datasLottery[i].lotteryCount != undefined) {
                    num2 = num2 + this.datasLottery[i].lotteryCount;
                }
            }
            if (num == 0) {
                let num3 = '0.00%';
                return num3;
            } else {
                let num3 = ((num2 / num) * 100).toFixed(2) + '%';
                return num3;
            }
        }
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
    onPageChange: function (num, type) {
        if (app.type == 1) {
            if (sessionStorage.pinggu != undefined) {
                app.username_proxy = sessionStorage.pinggu;
                sessionStorage.pinggu = '';
                app.getDeposit(num);
            } else {
                app.getDeposit(num);
            }
        } else {
            app.getLottery(num);
        }
    }
});
