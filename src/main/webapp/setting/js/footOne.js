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
        win: '',//详情列
        games: [],
        caiGuo: [],

        odd:[],     //记录原始数据用做修改比分
       
        matchId:'',//赛事编号
        prevData: '',//搜索记录
        pageNum: 50,  //默认每页5条
        total: 0,   //信息总条数
        league: '',//主队名或者客队名
        matchStatus: '',//开奖结果
        store_matchId:'',
        store_league:'',
    },
    created: function () {
        this.$nextTick(function () {
            this.getlayer();
            this.getGames();
        })
    },

    methods: {
        // 加载layer
        getlayer(){
            layui.use('layer', function () {
                let layer = layui.layer;
            })
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
                matchId:_this.matchId,//赛事编号
                "bannerNumber": num,
                "oneTypeId": 3
            };
            let obj = {
                type: 'get',
                data: datass,
                dataType: 'json',
                url: '/football/selectQiAndOneGame',
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
                            let brr = JSON.stringify(_this.datas);
                            _this.odd = JSON.parse(brr);
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
                            //截取时间
                            for (let i = 0; i < _this.datas.length; i++) {
                                _this.datas[i].matchDate = _this.datas[i].matchDate.slice(5, 16);
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
            _this.league=_this.store_league;
            _this.matchId=_this.store_matchId;
            let datass = {
                pageIndex: 1,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                league: _this.league.trim(),//主队名或者客队名
                matchStatus: _this.matchStatus,//开奖结果
                matchId:_this.matchId,//赛事编号
                "bannerNumber": num,
                "oneTypeId": 3
            };
            let obj = {
                type: 'get',
                data: datass,
                dataType: 'json',
                url: '/football/selectQiAndOneGame',
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
                            //截取时间
                            for (let i = 0; i < _this.datas.length; i++) {
                                _this.datas[i].matchDate = _this.datas[i].matchDate.slice(5, 16);
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
                    oneTypeId: 3
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
        //点击查看赔率详情
        clickSele: function (matchId) {
            let _this = this;
            let selectTwo = {
                type: 'get',
                data: {
                    'matchId': matchId
                },
                dataType: 'json',
                url: '/football/beijing/selectPeiLvByMatchid',
                success: function (data) {
                    if (data.code == 200) {
                        //取到后台传递多来的footballOddsInfo下面的数据
                        _this.win = data.body;
                        layer.open({
                            title: '查看赔率详情',
                            type: 1,
                            content: $('.analyMoreTable'),
                            area:['95%','95%'],
                        });
                    } else {
                        layer.msg('暂无数据');
                    }
                    //console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(selectTwo);
        },
        //更改比分
        changeFoot: function (matchId, letballNumber, aa, bb, cc, dd,index) {
            if (letballNumber === '' || aa === '' || bb === '' || cc === '' || dd === '') {
                layer.msg('输入框不能为空');
                return;
            }
            let data = {
                matchId: matchId,
                letballNumber: parseInt(letballNumber),
                courtScore: aa + ':' + bb,
                halfCourtScore: cc + ':' + dd,
            };
            //console.log(data);
            let obj = {
                type: "post",
                url: '/football/updateMatchInfoData',
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
            // var letballInputVal = $("[name=letball]").val();
            // letballInputVal = letballInputVal.replace(/^(-|\+)?\d+$/, "");
            // var halfInputVal = $("[name=half]").val();
            // halfInputVal = halfInputVal.replace(/^\d+:+\d$/, "");
            // var courtInputVal = $("[name=court]").val();
            // courtInputVal = courtInputVal.replace(/^\d+:+\d$/, "");
            // var _this = this;
            // if (!letballInputVal && !halfInputVal && !courtInputVal) {
            //     var data = {
            //         'matchId': matchId,
            //         'halfCourtScore': halfCourtScore,
            //         'letballNumber': letballNumber,
            //         'courtScore': courtScore,
            //     };
            //     ////console.log(data);
            //     var options = {
            //         type: "post",
            //         url: '/football/updateMatchInfoData',
            //         dataType: "json",
            //         data: data,
            //         success: function (data) {
            //             ////console.log(data);
            //             layer.msg(data.msg);
            //             _this.getdatas(1);
            //         },
            //         error: function (msg) {
            //             //console.log(msg)
            //         }
            //     };
            //     base.sendRequest(options);
            // }
        },
        //更改赔率详情
        changeFootWin: function (traditionOddsId, winWin, winDraw, winLose, drawWin, drawDraw, drawLose, loseWin, drawLose, loseLose, win10, win20, win21, win30, win31, win32, win40, win41, win42, winOther, draw00, draw11, draw22, draw33, drawOther, lose01, lose02, lose12, lose03, lose13, lose23, lose04, lose14, lose24, loseOther, upOdd, upEven, downOdd, downEven) {
            var data = {
                'traditionOddsId': traditionOddsId,
                'winWin': winWin,
                'winDraw': winDraw,
                'winLose': winLose,
                'drawWin': drawWin,
                'drawDraw': drawDraw,
                'drawLose': drawLose,
                'loseWin': loseWin,
                'drawLose': drawLose,
                'loseLose': loseLose,
                'win10': win10,
                'win20': win20,
                'win21': win21,
                'win30': win30,
                'win31': win31,
                'win32': win32,
                'win40': win40,
                'win41': win41,
                'win42': win42,
                'winOther': winOther,
                'draw00': draw00,
                'draw11': draw11,
                'draw22': draw22,
                'draw33': draw33,
                'drawOther': drawOther,
                'lose01': lose01,
                'lose02': lose02,
                'lose12': lose12,
                'lose03': lose03,
                'lose13': lose13,
                'lose23': lose23,
                'lose04': lose04,
                'lose14': lose14,
                'lose24': lose24,
                'loseOther': loseOther,
                'upOdd': upOdd,
                'upEven': upEven,
                'downOdd': downOdd,
                'downEven': downEven
            };
            ////console.log(data)
            ////console.log(winWin)
            ////console.log(winOther)
            var options = {
                type: "post",
                url: '/football/beijing/updatePeiLv',
                dataType: "json",
                data: data,
                success: function (data) {
                    ////console.log(data);
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