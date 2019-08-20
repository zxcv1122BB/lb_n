//显示灰色 jQuery 遮罩层 
function showBg() {
    let bh = $("body").height();
    let bw = $("body").width();
    $("#fullbg").css({
        display: "block"
    });
    $("#dialog").show();
}
//关闭灰色 jQuery 遮罩 
$("#fullbg").click(function () {
    closeBg();
});
function closeBg() {
    $("#fullbg,#dialog").hide();
}

//固定顶部
let oInfo = $(".main");
let oTop = $(".footTitle").offset().top;
let sTop = 0;
$(window).scroll(function () {
    sTop = $(this).scrollTop();
    if (sTop >= oTop) {
        $(".footTitle").css({"position": "fixed", "top": "0"});
    } else {
        $(".footTitle").css({"position": "static"});
    }
});

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
        games: [],
        matchId: '',
        letballCourtScore: '',
        letballNumber: '',
        courtScore: '',
        caiGuo: [],
        
        matchId:'',//赛事编号
        prevData: '',//搜索记录
        bannerNumber: '',
        pageNum: 50,  //默认每页5条
        total: 0,   //信息总条数
        league: '',//主队名或者客队名
        matchStatus: '',//开奖结果
    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.getGames();
        })
    },

    methods: {
        //加载layer
        getlayer(){
            layui.use('layer', function () {
                let layer = layui.layer;
            });
        },
        //利用ajax来查询记录列表
        getdatas: function (num1) {
            let _this = this;
            let num = $("#dateSelect").find("option:selected").val();
            if (num == '{{item}}') {
                num = ''
            }
            let datass = {
                pageIndex: num1,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                league: _this.league.trim(),//主队名或者客队名
                matchStatus: _this.matchStatus,//开奖结果
                bannerNumber: num/*this.bannerNumber*/,
                playTypeId: 14,
                matchId:_this.matchId //赛事编号
            };
            //console.info(datass);
            if (_this.prevData != "") {
                _this.prevData.pageIndex = num1;
                datass = _this.prevData;
            }
            let obj = {
                type: 'get',
                data: datass,
                dataType: 'json',
                url: '/football/selectSPF',
                success: function (data) {
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
                                if (_this.datas[i].halfCourtScore) {
                                    _this.datas[i].cc = _this.datas[i].halfCourtScore.split(':')[0];
                                    _this.datas[i].dd = _this.datas[i].halfCourtScore.split(':')[1];
                                } else {
                                    _this.datas[i].cc = '';
                                    _this.datas[i].dd = '';
                                }
                            }
                            //判断彩果
                            for (let j = 0; j < _this.datas.length; j++) {
                                if (_this.datas[j].courtScore != null) {
                                    let arr = _this.datas[j].courtScore.split(":");
                                    if (arr[0] > arr[1]) {
                                        _this.caiGuo[j] = "胜"
                                    } else if (arr[1] > arr[0]) {
                                        _this.caiGuo[j] = "负"
                                    } else {
                                        _this.caiGuo[j] = "平"
                                    }
                                }
                            }
                            //获取时间的时分
                            for (let i = 0; i < _this.datas.length; i++) {
                                _this.datas[i].matchDate = _this.datas[i].matchDate.slice(11, 16);
                                ////console.log(_this.datas[i].matchDate);
                            }
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,    //返回总页数
                                currentPage: 1                  //当前页
                            });
                        } else {
                            _this.datas = data.body.list;
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,                  //返回总页数
                                currentPage: 1                  //当前页
                            });
                        }
                    }
                    ////console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击查询
        search: function () {
            let _this = this;
            let num = $("#dateSelect").find("option:selected").val();
            if (num == '{{item}}') {
                num = ''
            }
            let datass = {
                pageIndex: 1,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                league: _this.league.trim(),//主队名或者客队名
                matchStatus: _this.matchStatus,//开奖结果
                bannerNumber: num/*this.bannerNumber*/,
                playTypeId: 14,
                matchId:_this.matchId //赛事编号
            };
            let obj = {
                type: 'get',
                data: datass,
                dataType: 'json',
                url: '/football/selectSPF',
                success: function (data) {
                    if (data.code == 200) {
                        //取到后台传递多来的Body下面的List
                        _this.prevData = obj.data;
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        if (data.body.list.length != 0) {
                            //判断彩果
                            for (let j = 0; j < _this.datas.length; j++) {
                                if (_this.datas[j].courtScore != null) {
                                    let arr = _this.datas[j].courtScore.split(":");
                                    if (arr[0] > arr[1]) {
                                        _this.caiGuo[j] = "胜"
                                    } else if (arr[1] > arr[0]) {
                                        _this.caiGuo[j] = "负"
                                    } else {
                                        _this.caiGuo[j] = "平"
                                    }
                                }
                            }
                            //获取时间的时分
                            for (let i = 0; i < _this.datas.length; i++) {
                                _this.datas[i].matchDate = _this.datas[i].matchDate.slice(11, 16);
                                ////console.log(_this.datas[i].matchDate);
                            }
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,    //返回总页数
                                currentPage: 1                  //当前页
                            });
                        } else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,                  //返回总页数
                                currentPage: 1                  //当前页
                            });
                        }
                    }
                    ////console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //获取期号
        getGames: function () {
            let _this = this;
            let obj = {
                type: 'get',
                data: {
                    oneTypeId: 2,
                },
                dataType: 'json',
                url: '/football/selectQi',
                success: function (data) {
                    if (data.code == 200) {
                        //取到后台传递多来的Body下面的List
                        _this.games = data.body;
                    }
                    ////console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //更改比分
        changeFoot: function (matchId, letballNumber, aa, bb, cc, dd) {
            if (letballNumber === '' || aa === '' || bb === '' || cc === '' || dd === '') {
                layer.msg('输入框不能为空');
                return;
            }
            let data = {
                matchId: matchId,
                letballNumber: letballNumber,
                courtScore: aa + ':' + bb,
                halfCourtScore: cc + ':' + dd,
            };
            ////console.log(data);
            let obj = {
                type: "post",
                url: '/football/updateMatchInfoData',
                dataType: "json",
                data: data,
                success: function (data) {
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

    },
    watch: {
        //监听页码每页条数
        pageNum: function () {
            this.getdatas(1);
        },
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
