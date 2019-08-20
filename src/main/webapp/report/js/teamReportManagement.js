
$(function(){
	$('.btns a').click(function(){ 
		$(this).addClass('active');
		$(this).siblings('a').removeClass('active');
	})
	
})

let app = new Vue({
    el:'#app',
    data:{
        infoDatas:{},
        datas:[],       //数据列表
        totalled:{},    //列表数据总计
        total:'',       //总条数
        pageNum:10,      //每页显示多少条数据，默认5条
        agent_store:'',
        type:3,
        uid:'',
        dateType:'month',
        autoSortUid:'',


        oldParam:{
            autoSortUid:'',
            type:''
        },

        isfirst:true,
        isClick:false,
        tipsLoad:''
    },
    created(){
        this.$nextTick(function () {
            this.getlayer();
            this.get_time();       //加载开始/结束日期控件
            this.month();
            // this.getdatas();
        });
    },
    mounted:function(){
        //赋值width
        var  wi=parseInt(document.body.clientWidth)-15;
        $(".layui-field-box th").width(parseFloat(wi/11).toFixed(2)+"px");
        $(".layui-field-box td").width(parseFloat(wi/11).toFixed(2)+"px");
    },
    methods:{
        //加载layer
        getlayer() {
            layui.use('layer', function () {
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
        getdatas(num,type1){
            if(this.isfirst){
                this.isfirst=false;
                return
            }
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                username:type1?_this.agent_store:'',
                uid:_this.uid,
                type:_this.type,
                dateType:_this.dateType,
                autoSortUid:_this.autoSortUid,
                startTime:$("#startDate").val(),
                endTime:$("#endDate").val()
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/reportManage/teamCount',
                success(data){

                    if(_this.tipsLoad){
                            layer.closeAll();
                            _this.tipsLoad="";
                            _this.isClick=false;
                        }
                    
                    if(data.code==200){
                        if(type1){
                            _this.infoDatas=data.body.pageInfo.list[0];
                        }else{
                            _this.datas = data.body.pageInfo.list;
                            _this.total = data.body.pageInfo.total;
                            if(data.body.pageInfo.list.length>0){
                                $('#fenye').jqPaginator('option', {
                                    totalPages: data.body.pageInfo.pages,    //返回总页数
                                    currentPage: 1
                                });
                            }else {
                                $('#fenye').jqPaginator('option', {
                                    totalPages: 1,    //返回总页数
                                    currentPage: 1
                                });
                                _this.resetting(1);
                            }   
                            if($("#popArea").is(":hidden")){
                                
                                _this.$nextTick(function(){
                                    layui.use('layer', function () {
                                            var layer = layui.layer;
                                            layer.open({
                                            type: 1,
                                            title: '下级详情',
                                            offset: '0',
                                            area: ['100%', '100%'],
                                            content: $('#popArea'),
                                            btn: ['关闭'],
                                            yes: function yes(index, layero) {
                                                layer.close(index);
                                                _this.uid = '';
                                                _this.autoSortUid =  _this.oldParam.autoSortUid;
                                                _this.type =  _this.oldParam.type;
                                            }
                                            });
                                        });
                                
                                });
                            }
                        }
                        
                    }else{
                        if(type1){
                            _this.infoDatas={
                                level:''
                            };
                        }
                    }
                },
                error(msg){
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击查询
        search(){
            var _this=this;
            if(!this.agent_store){
                layer.msg("请输入用户账号查询");
                return
            }
            this.type=3;
            this.tipsLoad=
                layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                setTimeout(function(){
                    _this.getdatas(1,2);
                },101);
                
        },
        //点击上级或下级
        sxj( item, type){
            
            if(!this.agent_store){
                return
            }
            if(this.isClick){
                return
            }
            this.tipsLoad=layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
              
            this.isClick=true;
            var _this = this;
            _this.oldParam={
                autoSortUid:_this.autoSortUid,
                type:_this.type
            };
        	_this.uid = item.uid;
        	_this.autoSortUid = type==1?'':item.autoSortUid;
        	// _this.agent_store='';
        	_this.type = type;
            setTimeout(function(){
                _this.getdatas(1);
            },101);
            
        },
        //点击重置
        resetting(type1){
           var _this = this;
            if(!type1){
                _this.agent_store='';
            }
        //    _this.agent_store='';
           _this.type=3;
           _this.autoSortUid='';
           _this.uid='';
           _this.dateType='month';
           _this.month();
        },
        //点击今日执行
        now: function () {
        	var _this = this;
        	_this.dateType="now";
        	var dateTime = new Date();
            dateTime.setTime(dateTime.getTime());
            var s2 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) 
            	+ "-" + _this.getzf(dateTime.getDate());
            $("#startDate").val(s2 + " " + "00:00:00");
            $("#endDate").val(s2 + " " + "23:59:59");
        },
        //点击昨日执行
        yes: function () {
        	var _this = this;
        	_this.dateType="yes";
        	var dateTime = new Date();
            dateTime.setTime(dateTime.getTime() - 24 * 60 * 60 * 1000);
            var s1 = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
            $("#startDate").val(s1 + " " + "00:00:00");
            $("#endDate").val(s1 + " " + "23:59:59");
        },
        //点击本周执行
        week: function () {
        	var _this = this;
        	_this.dateType="week";
        	var dateTime = new Date(),
            st = this.getDateTime(0),
            et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上周执行
        lastWeek: function () {
        	var _this = this;
        	_this.dateType="lastWeek";
        	var st = _this.getDateTime(2),
            et = _this.getDateTime(3);
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击本月执行
        month: function () {
        	let _this = this;
        	_this.dateType="month";
        	var dateTime = new Date(),
            st = _this.getDateTime(4),
            et = dateTime.getFullYear() + "-" + _this.getzf(dateTime.getMonth() + 1) + "-" + _this.getzf(dateTime.getDate());
	        $("#startDate").val(st + " " + "00:00:00");
	        $("#endDate").val(et + " " + "23:59:59");
        },
        //点击上月执行
        lastMonth: function () {
        	let _this = this;
        	_this.dateType="lastMonth";
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
    },
    //监听
    watch:{
        pageNum:function () {
            this.getdatas(1);
        }
    },
    //计算属性
    computed:{
        //投注小计
        total_betAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){ 
                if(this.datas[i].betAmount != undefined){
                    num = num + parseFloat(this.datas[i].betAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },

        total_betCount:function(){
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].betCount != undefined){
                    num = num + parseFloat(this.datas[i].betCount);
                }
            }
            return num;
        },

        //投注返利
        total_betRebate:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].betRebate != undefined){
                    num = num + parseFloat(this.datas[i].betRebate.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //中奖金额
        total_bonusAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].bonusAmount != undefined){
                    num = num + parseFloat(this.datas[i].bonusAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //存款赠送小计
        total_cashinAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].cashinAmount != undefined){
                    num = num + parseFloat(this.datas[i].cashinAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //注册赠送小计
        total_firstCashinMoneyCount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].firstCashinMoneyCount != undefined){
                    num = num + parseFloat(this.datas[i].firstCashinMoneyCount);
                }
            }
            return num;
        },
        //提款小计
        total_cashinGive:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].cashinGive != undefined){
                    num = num + parseFloat(this.datas[i].cashinGive.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //返点小计
        total_registerGive:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].registerGive != undefined){
                    num = num + parseFloat(this.datas[i].registerGive.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //手动加款
        total_registerUserCount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].registerUserCount != undefined){
                    num = num + parseFloat(this.datas[i].registerUserCount);
                }
            }
            return num;
        },
        //人工赠送
        total_cashoutAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].cashoutAmount != undefined){
                    num = num + parseFloat(this.datas[i].cashoutAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        //手动扣款
        total_rebateAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].rebateAmount != undefined){
                    num = num + parseFloat(this.datas[i].rebateAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_artificialPlus:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].artificialPlus != undefined){
                    num = num + parseFloat(this.datas[i].artificialPlus.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_artificialGive:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].artificialGive != undefined){
                    num = num + parseFloat(this.datas[i].artificialGive.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_artificialSub:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].artificialSub != undefined){
                    num = num + parseFloat(this.datas[i].artificialSub.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_downAgentCount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].downAgentCount != undefined){
                    num = num + parseFloat(this.datas[i].downAgentCount);
                }
            }
            return num;
        },
        //团队盈亏
        total_profit:function () {
            let num = 0;
            let abs = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].profit != undefined){
                	if(parseFloat(this.datas[i].profit)<0){
                		abs = abs + Math.abs(this.datas[i].profit.replace(/[,]/g,''))
                	}else{
                		num = num + parseFloat(this.datas[i].profit.replace(/[,]/g,''));
                	}
                } 
            }
            abs = parseFloat(abs);
            num = parseFloat(num);
            return (num-abs).toFixed(2);
        },
        //总计数据
        total_betSystemAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].betSystemAmount != undefined){
                    num = num + parseFloat(this.datas[i].betSystemAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_betDigitalAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].betDigitalAmount != undefined){
                    num = num + parseFloat(this.datas[i].betDigitalAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
         
        total_bonusSystemAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].bonusSystemAmount != undefined){
                    num = num + parseFloat(this.datas[i].bonusSystemAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_bonusDigitalAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].bonusDigitalAmount != undefined){
                    num = num + parseFloat(this.datas[i].bonusDigitalAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        

        
        total_systemAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].systemAmount != undefined){
                    num = num + parseFloat(this.datas[i].systemAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        total_digitalAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].digitalAmount != undefined){
                    num = num + parseFloat(this.datas[i].digitalAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        },
        
        total_winAmount:function () {
            let num = 0;
            for(let i = 0;i<this.datas.length;i++){
                if(this.datas[i].winAmount != undefined){
                    num = num + parseFloat(this.datas[i].winAmount.replace(/[,]/g,''));
                }
            }
            return parseFloat(num).toFixed(2);
        }
    }
});


// 加载分页功能
$.jqPaginator('#fenye', {
    totalPages: 1,      //多少页数据
    visiblePages: 10,   //最多显示几页
    currentPage: 1,     //当前页
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