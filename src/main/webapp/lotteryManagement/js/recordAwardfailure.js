/**
 * Created by ASUS on 2017/9/29.
 */
let app = new Vue({
    el: "#app",
    data: {
        datas: [],//方案列表
        LotteryTypes: [], // 彩票列表
        userParticulars: [], //用户详情列表
        ordersParticulars: [], //订单详情列表

        page_num: 50,
        total: "", //信息总条数
        roleId: localStorage.roleId,
        /*搜索绑定*/
        username: "", //用户名
        status: "", //状态
        outOfThrity: "0",  //时间
        startTime: "", //时间开始
        endTime: "", //时间停止
        lowerAmount: "", //最低金额
        higherAmount: "", //最高金额
        lowerBonus: "", //最低奖金
        higherBonus: "", //最高奖金
        gameType: "", //彩票种类
        id: '',
        userLast: [],//
        actionMes: [], //投注比赛详情
    },
    created() {
        this.getlayer();
        this.get_time();       //加载开始/结束日期控件
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
        //加载选择开始/结束日期控件
        get_time: function () {
            //	日期设置
            laydate.render({
                elem: '#startDate', //指定元素
                format: 'yyyy-MM-dd',
                type: 'datetime',
            });
            laydate.render({
                elem: '#endDate', //指定元素,
                format: 'yyyy-MM-dd',
                type: 'datetime',
                // max: 0,
                // value: "",
            })
        },
        //获取方案列表
        getdatas: function (num) {
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: this.page_num,
                pageSize: 5,

                username: _this.username, //用户名
                status: _this.status, //状态
                outOfThrity: _this.outOfThrity,   //时间
                startTime: _this.startTime, //时间开始
                endTime: _this.endTime, //时间停止
                lowerAmount: _this.lowerAmount, //最低金额
                higherAmount: _this.higherAmount, //最高金额
                lowerBonus: _this.lowerBonus, //最低奖金
                higherBonus: _this.higherBonus, //最高奖金
                gameType: _this.gameType //彩票种类
            };
            let options = {
                type: "get",
                url: "/bets/queryFailOrderInfoList",
                data: data,
                dataType: 'json',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        if(data.body.pages!=0){
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,
                                currentPage:1
                            });
                        }else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage:1
                            });
                        }

                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        /*   getdatas1:function(){
               var _this = this;
               var data={};
               var options={
                   type:"get",
                   url:"/call/openmanage",
                   data:data,
                   dataType: 'json',
                   success:function(data){
                       //console.log(data);

                   },
                   error: function (msg) {
                       //console.log(msg);
                   }
               };
               base.sendRequest(options);
           },*/
        //手动开奖操作
        manualwork: function (id, judge) {
            //console.log(id);
            var _this = this;
            var data = {};
            var options = {
                type: "get",
                url: "/call/openmanage",
                data: {"id": id, "judge": judge},
                dataType: 'json',
                success: function (data) {
                    var msg = data.msg;
                    //弹出信息
                    layer.open({
                        title: '开奖处理结果'
                        , content: msg
                    });
                },
                error: function (msg) {

                }
            };
            base.sendRequest(options);
        },
        //获取搜索彩票种类
        /*       getLotteryTypes:function(){
                   var _this=this;
                   var options={
                       type:"get",
                       url:"/bets/queryGameType",
                       data:{},
                       dataType: 'json',
                       success:function(data){
                           ////console.log(data)
                           if(data.code==200){
                               _this.LotteryTypes=data.body;
                           }
                       },
                       error:function(msg){
                           //console.log(msg)
                       }
                   };
                   base.sendRequest(options);
               },*/

        //方案搜索
        searchScheme: function () {
            var _this = this
            var data = {
                pageIndex: 1,
                pageNum: this.page_num,
                pageSize: 5,

                username: _this.username, //用户名
                status: _this.status, //状态
                outOfThrity: _this.outOfThrity,   //时间
                startTime: _this.startTime, //时间开始
                endTime: _this.endTime, //时间停止
                lowerAmount: _this.lowerAmount, //最低金额
                higherAmount: _this.higherAmount, //最高金额
                lowerBonus: _this.lowerBonus, //最低奖金
                higherBonus: _this.higherBonus, //最高奖金
                gameType: _this.gameType //彩票种类
            };
            var options = {
                type: "get",
                url: "/bets/queryFailOrderInfoList",
                data: data,
                dataType: "json",
                success: function (data) {
                    //console.log(data)
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        //分页的(右边点击)
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },
        //开奖错误信息
        errorMsg: function (errormsg) {

            layer.open({
                title: '错误信息'
                , content: errormsg
            });

        },
        //点击用户名
        biddingUser: function (name) {
            var _this = this;
            var options = {
                type: "get",
                url: "/bets/queryUserDetail",
                data: {
                    username: name
                },
                dataType: "json",
                success: function (data) {
                    layui.use('layer', function () {
                        var layer = layui.layer;
                        layer.open({
                            title: '查看会员详情',
                            type: 1,
                            content: $('.concealDiv'),
                            area: ['60%'],
                            btn: ['关闭'],
                            yes: function (index, layero) {
                                layer.closeAll('page');
                            }
                        })
                    })
                    //console.log(data)
                    if (data.code == 200) {
                        _this.userParticulars = data.body;
                        _this.userLast = data.body.loginLog;
                        for (var i = 0; i < _this.userLast.length; i++) {
                            _this.userLast.loginTime = _this.userLast[0].loginTime
                            _this.userLast.loginIp = _this.userLast[0].loginIp
                        }
                    }
                },
                error: function (msg) {
                    //console.log(msg)
                }
            }
            base.sendRequest(options);
        },
        //点击订单表
        orders: function (orderId) {
            var _this = this;
            var options = {
                type: "get",
                url: "/bets/queryBettingOrderDetail",
                data: {
                    orderId: orderId,
                    outOfThrity: _this.outOfThrity,
                },
                success: function (data) {
                    layui.use('layer', function () {
                        var layer = layui.layer;
                        layer.open({
                            title: '查看订单详情',
                            type: 1,
                            content: $('.concealDivOrders'),
                            area: ['60%'],
                            btn: ['关闭'],
                            yes: function (index, layero) {
                                layer.closeAll('page');
                            }
                        })
                    })
                    //console.log(data)
                    _this.ordersParticulars = data.body;
                },
                error: function (msg) {
                    //console.log(msg)
                }
            }
            base.sendRequest(options);

        },
        //点击查看投注比赛详情
        actionData: function (orderId) {
            var _this = this;
            var options = {
                type: "get",
                url: "/bets/queryBettingOrderDetail",
                data: {
                    "orderId": orderId
                },
                dataType: "json",
                success: function (data) {
                    layui.use('layer', function () {
                        var layer = layui.layer;
                        layer.open({
                            title: '查看投注比赛详情',
                            type: 1,
                            content: $('.actionData'),
                            area: ['60%'],
                            btn: ['关闭'],
                            yes: function (index, layero) {
                                layer.closeAll('page');
                            }
                        })
                    })
                    //console.log(data)
                    if (data.code == 200) {
                        _this.actionMes = data.body.list;
                        for (var i = 0; i < _this.actionMes.length; i++) {
                            if (_this.actionMes[i].matchResult == 3) {
                                _this.actionMes[i].matchResult = "胜"
                            } else if (_this.actionMes[i].matchResult == 1) {
                                _this.actionMes[i].matchResult = "平"
                            } else {
                                _this.actionMes[i].matchResult = "负"
                            }
                        }

                    }
                },
                error: function (msg) {
                    //console.log(msg)
                }
            }
            base.sendRequest(options);
        },
        //隐藏用户详情和订单详情
        quit: function () {
            this.detailUser = false;
            this.isShow = false;
            this.ordersUser = false;
        },
        //点击清空
        clear: function () {
            $(".table input").val("");
        },

    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
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