//获取日期
/*function GetDateStr(AddDayCount) {
 let dd = new Date();
 dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
 let y = dd.getFullYear();
 let m = dd.getMonth() + 1;//获取当前月份的日期
 let d = dd.getDate();
 return y + "-" + m + "-" + d;
 }
 for (let i = 0; i > -9; i--) {
 $("#dateSelect").append("<option value='" + GetDateStr(i) + "'>" + GetDateStr(i) + "</option>");
 }*/

//文本框禁用空格
function keyUp() {
    if (event.keyCode == 32) {
        event.returnValue = false;
    }
}

//数据交互
let app = new Vue({
    el: '.main',
    data: {
        datas: [], //记录列表
        pageNum: 50,  //默认每页5条
        total: '',   //信息总条数

        odd:[],   //记录原始数据用做修改比分

        win: '', //赔率详情
        //更改比分
        matchId: '',
        letballCourtScore: '',
        letballNumber: '',
        courtScore: '',
        caiGuo: [],

        // prevData: '',//搜索记录
        league: '',//主队名或者客队名
        matchStatus: '',//开奖结果

        no_end: '',      //监听未结束时间
    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.get_time();       //加载开始/结束日期控件
        })
    },

    methods: {
        //加载layer
        getlayer(){
            layui.use('layer', function () {
                let layer = layui.layer;
            });
        },
        //加载选择开始/结束日期控件
        get_time: function () {
            //	日期设置
            laydate.render({
                elem: '#startDate', //指定元素
                format: 'yyyy-MM-dd ',
                type: 'datetime',
            });
            laydate.render({
                elem: '#endDate', //指定元素,
                format: 'yyyy-MM-dd ',
                type: 'datetime',
            })
        },
        //利用ajax来查询记录列表
        getdatas: function (num) {
            let start = $('#startDate').val();
            let end = $('#endDate').val();
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                startTime: start,
                endTime: end,
                league: this.league.trim(),//主队名或者客队名
                matchStatus: this.matchStatus,//开奖结果
                hour: this.no_end,
                matchId:this.matchId.trim()  //赛事编号
            };
            // if (_this.prevData != "") {
            //     _this.prevData.pageIndex = num;
            //     datass = _this.prevData;
            // }
            let _this = this;
            ////console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/basketball/selectBasketballForJC',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        if (data.body.list.length != 0) {
                            //取到后台传递多来的Body下面的List
                            _this.datas = data.body.list;
                            _this.total = data.body.total;
                            for (let i = 0; i < _this.datas.length; i++) {
                                if (_this.datas[i].courtScore) {
                                    _this.datas[i].aa = _this.datas[i].courtScore.split(':')[0];
                                    _this.datas[i].bb = _this.datas[i].courtScore.split(':')[1];
                                } else {
                                    _this.datas[i].aa = '';
                                    _this.datas[i].bb = '';
                                }
                            }
                            let brr = JSON.stringify(_this.datas);
                            _this.odd = JSON.parse(brr);
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,    //返回总页数
                                currentPage: 1                  //当前页
                            });
                        } else {
                            _this.datas = data.body.list;
                            _this.total = data.body.total;
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,                  //返回总页数
                                currentPage: 1                  //当前页
                            });
                        }
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击查询
        search: function () {
            this.no_end = '';
            this.getdatas(1);
        },
        //查看赔率详情
        clickSele: function (matchId) {
            let _this = this;
            let selectTwo = {
                type: 'get',
                data: {
                    'id': matchId
                },
                dataType: 'json',
                url: '/basketball/selectOddsinfoByMatchId',
                success: function (data) {
                    if (data.code == 200) {
                        //取到后台传递多来的Body下面的数据
                        _this.win = data.body;
                        layer.open({
                            title: '查看赔率详情',
                            type: 1,
                            content: $('.analyMoreTable'),
                            area: ['95%', '95%'],
                        })
                    } else {
                        layer.msg('暂无数据！');
                    }
                    ////console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };

            base.sendRequest(selectTwo);
        },
        //更改比分
        changeFoot: function (matchId, letScore,expectTotalScore,aa, bb,index) {
            if (letScore === '' || aa === '' || bb === '') {
                layer.msg('输入框不能为空');
                return;
            }
            let data = {
                matchId: matchId,
                letScore: letScore,
                courtScore: aa + ':' + bb,
                expectTotalScore:expectTotalScore
            };
            ////console.log(data);
            let obj = {
                type: "post",
                url: '/basketball/updateMatchInfoData',
                dataType: "json",
                data: data,
                success: function (data) {
                    ////console.log(data);
                    layer.msg(data.msg);
                    setTimeout(function () {
                        window.location.reload();
                    },500)
                },
                error: function (msg) {
                    //console.log(msg)
                }
            };
            base.sendRequest(obj);
        },
        changeFootWin: function (id,homeWin,homeLose,letscoreWin,letscoreLose,bigScore,smallScore,win15,win610,win1115,win1620,win2125,win26) {
            let data = {
                'id': id,
                'homeWin': homeWin,
                'homeLose': homeLose,
                'letscoreWin': letscoreWin,
                'letscoreLose': letscoreLose,
                'bigScore': bigScore,
                'smallScore': smallScore,
                'win15': win15,
                'win610': win610,
                'win1115': win1115,
                'win1620': win1620,
                'win2125': win2125,
                'win26': win26,
            };
            //console.log(data);
            // //console.log(winWin);
            ////console.log(winOther);
            let options = {
                type: "post",
                url: '/basketball/updatePeiLv',
                dataType: "json",
                data: data,
                success: function (data) {
                    //console.log(data);
                    layer.msg(data.msg);
                },
                error: function (msg) {
                    //console.log(msg)
                }
            };
            base.sendRequest(options);
        },


    },
    watch: {
        //监听页码每页条数
        pageNum: function () {
            this.getdatas(1);
        },
        //未结束的时间
        no_end(){
            if (this.no_end != '') {
                $('#startDate').val('');
                $('#endDate').val('');
                this.matchStatus = '';
                this.league = '';
                this.getdatas(1);
            } else {
                $('#startDate').val('');
                $('#endDate').val('');
                this.matchStatus = '';
                this.league = '';
                this.no_end = '';
                this.getdatas(1);
            }
        }
    }
});
//加载分页功能
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