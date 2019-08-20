//定义图形变量
let todayBounsPercent = 0.00,//今日中奖百分比
    todayLossPercent = 0.00,//今日盈亏百分比
    yesterBounsPercent = 0.00,//昨日中奖百分比
    yesterLossPercent = 0.00;//昨日盈亏百分比

//使用vue.js
let app = new Vue({
    el: '#app',
    data: {
        datas: [], //记录列表
        total: 0, //信息总条数
        pageNum: 50,

        //今日投注
        todayAmount: 0.00,//今日投注金额
        todayLoss: 0.00,//今日盈亏金额
        yesterAmount: 0.00,//昨日投注金额
        yesterLoss: 0.00,//昨日盈亏金额

        ob:{},         //概况
    },
    created: function () {
        this.getchats();         //图形
        this.getdatas();        //列表数据
        this.get_survey();      //获取概况
    },

    //利用ajax来查询记录列表
    methods: {
        //获取图形数据
        getchats: function () {
            let _this = this;
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/reportManage/queryTodayAndYesterDayMoney',
                success: function (data) {
                    if (data.code == 200) {
                        _this.todayAmount = data.body.toDayAmount;
                        _this.todayLoss = data.body.toDayLoss;
                        _this.yesterAmount = data.body.yesterDayAmount;
                        _this.yesterLoss = data.body.yesterDayLoss;
                        todayBounsPercent = data.body.toDayBounsPercent;
                        todayLossPercent = data.body.toDayLossPercent;
                        yesterBounsPercent = data.body.yesterDayBounsPercent;
                        yesterLossPercent = data.body.yesterDayLossPercent;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //获取每月各项统计记录列表
        getdatas: function (num) {
            let _this = this;
            if (num == null) {
                num = 1;
            }
            let obj = {
                type: 'get',
                data: {
                    'pageIndex': num,
                    'pageNum': 5,
                    'pageSize': 10
                },
                dataType: 'json',
                url: '/reportManage/queryReportMonthSummaryPage',
                success: function (data) {
                    if (data.code == 200) {
                        //取到后台传递多来的Body下面的List
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        // if (data.body.list.length > 0) {
                        //     $('#fenye').jqPaginator('option', {
                        //         totalPages: data.body.pages, //返回总页数
                        //         currentPage: 1
                        //     });
                        // } else {
                        //     $('#fenye').jqPaginator('option', {
                        //         totalPages: 1, //返回总页数
                        //         currentPage: 1
                        //     });
                        // }
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //获取统计概况
        get_survey(){
            let _this = this;
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/reportManage/queryCountGK',
                success(data){
                    ////console.log(data);
                    if(data.code==200){
                        //console.log(data.body);
                        _this.ob = data.body;
                    }
                },
                error(msg){
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        }
    },
    watch: {
        //监听下拉框的值(每页显示多少条数据)
        pageNum: function () {
            this.getdatas(1);
        },
    }
});


// //加载分页功能
// $.jqPaginator('#fenye', {
//     totalPages: 1, //多少页数据
//     visiblePages: 5, //最多显示几页
//     currentPage: 1, //当前页
//     wrapper: '<ul class="pagination"></ul>',
//     first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
//     prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
//     next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
//     last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
//     page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
//     onPageChange: function (num) {
//         app.getdatas(num);
//     }
// });

$(function () {
    //图形数据
    $('#today_pie').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        legend: {
            align: 'center', //水平方向位置
            verticalAlign: 'top', //垂直方向位置
            x: 0, //距离x轴的距离
            y: 20 //距离Y轴的距离
        },
        title: {
            text: ''
        },
        colors: ['#fe6383', '#4db2e6'],
        tooltip: {
            headerFormat: '{series.name}<br>',
            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                size: 185,
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: ' ',
            data: [
                ['今日中奖额', todayBounsPercent],
                ['今日盈亏', todayLossPercent],
            ]
        }]
    });

    $('#yesterday_pie').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        legend: {
            align: 'center', //水平方向位置
            verticalAlign: 'top', //垂直方向位置
            x: 0, //距离x轴的距离
            y: 20 //距离Y轴的距离
        },
        title: {
            text: ''
        },
        colors: ['#fe6383', '#4db2e6'],
        tooltip: {
            headerFormat: '{series.name}<br>',
            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                size: 185,
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: ' ',
            data: [
                ['昨日中奖额', yesterBounsPercent],
                ['昨日盈亏', yesterLossPercent],
            ]
        }]
    });
});