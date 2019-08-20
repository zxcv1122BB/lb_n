/**
 * Created by ASUS on 2017/9/12.
 */

$(function () {
    $('.newTab a').click(function () {
        $(this).siblings().removeClass('curr');
        $(this).addClass('curr');
    });
});
let app = new Vue({
    el: '#app',
    data: {
        pageNum: 50, //默认每页5条数据-绑定
        datas: [], //列表数据
        total: '', //总条数-绑定

        grade: [], //会员等级数组
        names: [], //按筛选条件名称数组

        obj: {}, //个人详情数据
        exportUser: base.BASE_URL,
        //下面参数是搜索时要传的:
        startDate: '', //开始时间
        endDate: '', //结束时间
        username: '', //会员帐号-绑定
        parentName: '', //所属上级-绑定
        referee: '',		//推荐人
        vipName: '', //会员等级名称-绑定
        keywordOption: 'NAME', //会员筛选下拉名称-绑定
        keywordValue: '', //关键字-绑定
        rechargeSituation: 0, //充值情况-绑定
        coin: '', //余额-绑定
        loginDays: '', //多少天未登录

        //修改会员信息绑定要传的参数
        uid: '', //uid
        zhanghao: '', //登录帐号
        daili: '', //指定代理
        dailis_: [], //代理数组
        proxy_id: '', //要传的代理id
        flag_: false, //控制要传的代理id是否存在
        all_proxy: [], //所有代理
        name: '', //会员姓名
        iphone: '', //手机号
        //flag_tel: true, //检测手机格式是否正确
        weixin: '', //微信
        email: '', //邮箱
        //flag_email: true, //检测邮箱格式是否正确
        line: '', //QQ
        rank: '', //银行帐号
        rank_hao: '', //银行帐号
        rank_address: '', //开户分行
        remark: '', //备注
        vip: '', //会员等级-下拉
        rank_black: '', //银行卡黑名单-下拉
        state: '', //会员状态-下拉
        reg_ip: '', //注册ip
        channel: '', //注册来源
        system: '', //注册系统
        parents: '', //所属上级

        //修改登录密码
        uid1: '', //uid
        zhanghao1: '', //帐号
        pssword: '', //密码
        rePssword: '', //确认密码

        //修改资金密码
        uid3: '', //uid
        zhanghao3: '', //帐号
        pssword3: '', //密码
        rePssword3: '', //确认密码

        //新增会员
        zhanghao2: '', //帐号
        flag1: false, //控制要传的帐号是否存在
        daili2: '', //代理
        password2: '', //密码
        password2_: '', //确认密码
        name2: '', //姓名
        iphone2: '', //手机号
        flag_tel2: false, //检测手机格式是否正确
        email2: '', //邮箱
        line2: '', //qq
        weixin2: '', //微信
        rank2: '', //银行帐号
        rank_hao2: '', //银行帐号
        rank_address2: '', //开户分行
        vip_: 0, //会员等级
        state_: 1, //会员状态
        dailis: [], //代理数组
        daili_id: '', //要传的代理id
        flag: false, //控制要传的代理id是否存在
        regSystem: '', //注册系统--js获取

        coinSort: '', //余额排序
        timeSort: '', //时间排序
        statusSort: '', //会员状态排序
        onlineSort: '', //在线状态排序

        Authorization: '', //导出会员功能时要记录的请求头参数，从acessToken中获取
        //添加一组数据存储变量，当用户点击分页时不直接将搜索框中的数据传递到查询条件中，只有点击搜索时才将条件带入查询条件
        store_username: '',
        store_loginAccount: '',
        store_vipName: '',
        store_keywordOption: 'NAME',
        store_keywordValue: '',
        store_rechargeSituation: '',
        store_coin: '',
        store_loginDays: '',
        store_regSystem: '',
        store_parentName: '',
        store_referee: '',

        LoginTime: '',//最后登录时间

        //接口请求的返点数据
        rebateConfigList: [],
        //代理
        userType: 0,
        //返点设置
        //无代理时使用返点
        emptyRebateGameList: [],
        //当前返点
        rebateGameList: [],
        rebateGame: "",
        //返点存储
        rebateParamList: [],

        topStatus: 0,
        pageIndex: 1,   //当前页
        itemRebateList:[],

        //玩法最大返点列表
        maxRebatesList:[],
        nameRebatesList:[],
        modelRebatesList:[],
        downList:[],
    },
    created: function () {
        this.getRebateData();
        this.$nextTick(function () {
            this.getlayer();
            this.get1(); //获取会员等级名称
            this.get2(); //获取筛选条件名称
            this.get_time(); //加载开始/结束日期控件
        });
        //console.log(this.rebateGameList);
    },
    mounted: function () {
        this.clickBtn();

    },
    methods: {
        //邀请码管理
        InviteCode: function (obj) {
            var _this = this;
            _this.uid = obj.uid;
            var rebatedata = obj.data,
                codeNameList = [], _this = this;
            rebatedata = rebatedata.split("@");
            rebatedata.map(function (item, index) {
                rebatedata[index] = item.split("#");
                codeNameList.push(rebatedata[index][0]);
            });

            _this.rebateConfigList.map(function (item, sIndex) {
                var oIndex = codeNameList.indexOf(item.code);
                if (oIndex != -1) {
                    _this.rebateConfigList[sIndex].rebate = rebatedata[oIndex][1];
                }
            })
            layer.open({
                title: '邀请码管理',
                type: 1,
                content: $('.inviteCode'),
                area: ['50%', '80%'],
                yes: function (index) {

                },
                btn2: function () {
                }
            })

        },

        //生成邀请码
        creadtedCode: function () {
            var _this =this;
            var check = this.checkRebatesFn();
            if (check.check == 1) {
                return
            }
            var data1= {
                userType: parseInt(_this.userType) + 1,
                    data: check.str,
                    proxyId: _this.uid
            };
            var obj = {
                    type: "post",
                    url: "/user/addInvitateInfo",
                    data: data1,
                    success: function (data) {
                        if (data.code == 200) {
                            // _this.rebateGameList.map(function (item, index) {
                            //     _this.$set(_this.rebateGameList, index, "");
                            // });
                            layui.use('layer', function () {
                                var layer = layui.layer;
                                layer.msg("已成功生成！");
                            });
                            _this.topStatus=1;
                        } else {

                        }
                    },
                    error: function (msg) {
                        //console.log(msg)
                    },
                };
            base.sendRequest(obj);

        },

        //生成邀请码的参数验证
        checkRebatesFn:function () {
            // data(彩种返点拼接字符串 eg: ssc#8.0@k3#8.5@11x5#7.5@3D#7.5@PK10#8.0@hk6#10.0)
            var _this=this,check=0,isNoContinue=0,str="";
            _this.rebateGameList.map(function(item,index){
                var text1=parseFloat(item),
                    text2 = parseFloat(_this.rebateConfigList[index].rebate);

                if (isNoContinue==0){
                    if(str){
                        str += "@" + _this.rebateConfigList[index].code + "#" + item;
                    }else{
                        str += _this.rebateConfigList[index].code + "#" + item;
                    }

                    if (isNaN(text1)) {
                        layui.use('layer',function(){
                            var layer=layui.layer;
                            layer.msg(_this.rebateConfigList[index].codeName ? _this.rebateConfigList[index].codeName : _this.rebateConfigList[0].codeName+"返点设置不正确");
                        });
                        _this.$set(_this.rebateGameList, index, "");
                        isNoContinue =1;
                        check = 1;
                    } else if (text1 < 0) {
                        layui.use('layer',function(){
                            var layer=layui.layer;
                            layer.msg(_this.rebateConfigList[index].codeName + "返点要大于0.0");
                        });
                        _this.$set(_this.rebateGameList, index, 0.0);
                        isNoContinue = 1;
                        check = 1;
                    } else if (text1 > text2) {

                        layui.use('layer',function(){
                            var layer=layui.layer;
                            layer.msg(_this.rebateConfigList[index].codeName + "返点要小于" + text2.toFixed(1));
                        });
                        _this.$set(_this.rebateGameList, index, text2.toFixed(1));
                        isNoContinue = 1;
                        check = 1;
                    }
                }

            });
            return {
                "check":check,
                "str":str
            };
        },


        //邀请码加载数据
        loadList:function() {
            var _this = this,
                obj = {
                    type: "post",
                    url: "/user/queryInvitateInfoList",
                    data: {
                        userType: parseInt(_this.userType)+1,
                        pageIndex: _this.pageIndex,
                        pageNum: 10,
                        proxyId: _this.uid,
                        pageSize:5
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            _this.downList = data.body.list;

                            //分页的(右边点击)
                            if(data.body.list.length>0){
                                $('#pages').jqPaginator('option', {
                                    totalPages: data.body.pages,    //返回总页数
                                    currentPage: data.body.pageNum
                                });
                            }else {
                                $('#pages').jqPaginator('option', {
                                    totalPages: 1,
                                    currentPage: 1
                                });
                            }
                        } else {
                            _this.downList = []
                        }
                    },
                    error: function (msg) {
                        //console.log(msg)
                    },
                };
            base.sendRequest(obj);
        },

        //邀请码详情
        showPopover:function(index){



            var _this=this,
                str = _this.downList[index].data;
            str=str.replace("ssc","时时彩").replace("k3","快3").replace("11x5","11选5").replace("3D","福彩3D").replace("PK10","PK10").replace('hk6','六合彩').replace('7xc','七星彩').replace('kl10f','快乐十分').replace('PCDD','北京28');
            str=str.split("@");
            str.map(function(item,index){
                str[index] = item.split("#");
            });
            _this.itemRebateList=str;
            _this.invitateId = _this.downList[index].invitateId;
            _this.$nextTick(function () {
                window.parent.layui.use('layer', function () {
                    var layer = window.parent.layui.layer;
                    layer.open({
                        type: 1,
                        title: '返点详情',
                        area: ['37%', '37%'], //宽高
                        content: $('#popover').html(),
                        btn: ['关闭'],
                        yes: function (index, layero) {
                            window.parent.layer.closeAll('page');
                        }
                    });
                })
            })

        },

        //邀请码删除
        delItemRebate(inId,index){
            var _this = this;
            _this.invitateId = _this.downList[index].invitateId;
            window.parent.layui.use('layer', function () {
                var layer_confirm = window.parent.layui.layer;
                layer_confirm.open({
                    content: "<div style='padding: 0 80px 0 20px;height: 42px;line-height: 42px;font-size: 14px;'>确定删除?</div>",
                    area: "400px",
                    type: 1,
                    closeBtn: 0,
                    title: "提示",
                    btn: ["确定", "取消"],
                    yes: function () {
                        window.parent.layer.closeAll('page');
                        $('body').css('overflow', "auto");
                        var obj = {
                            type: "post",
                            url: "/user/removeInvitateInfo",
                            data: {
                                invitateId: inId,
                                proxyId: _this.uid
                            },
                            success: function (data) {  
                                if (data.code == 200) {
                                    layer.msg("删除成功");
                                    _this.loadList();
                                }
                            },
                            error: function (msg) {
                                //console.log(msg)
                            },
                        };
                        base.sendRequest(obj);
                    },
                    btn2: function () {
                        $('body').css('overflow', "auto");
                    },
                });
            });


        },


        //加载返点数据
        getRebateData: function () {
            var _this = this,
                obj = {
                    type: "post",
                    data: {},
                    dataType: 'json',
                    url: "/user/getRebateConfigList",
                    success: function (data) {
                        if (data.code == 200) {
                            _this.rebateGameList = [];
                            for (var i = 0, len = data.body.length; i < len; i++) {
                                _this.rebateGameList.push(data.body[i].rebate);
                            }
                            _this.emptyRebateGameList = data.body;
                            _this.rebateConfigList = data.body;
                        }
                    },
                    error: function () {

                    },
                };
            base.sendRequest(obj);
        },
        //加载layer
        getlayer() {
            layui.use('layer', function () {
                let layer = layui.layer;
            })
        },
        //初始化获取数据
        getdatas: function (num) {
            this.Authorization = localStorage.acessToken;
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,

                startTime: this.startDate,
                endTime: this.endDate,
                username: this.username,
                loginAccount: this.parentName,
                parentName: this.referee,
                vipName: this.vipName,
                keywordOption: this.keywordOption,
                keywordValue: this.keywordValue,
                rechargeSituation: this.rechargeSituation,
                coin: this.coin,
                loginDays: this.loginDays,
                regSystem: this.regSystem,

                //下面是排序要传的参数，默认是由时间由新到旧排序
                coinSort: this.coinSort,
                timeSort: this.timeSort, //时间由新到旧-默认
                statusSort: this.statusSort,
                onlineSort: this.onlineSort
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/user/queryUserList',
                success: function (data) {
                    if (data.code == 200) {
                        _this.total = data.body.total;
                        _this.datas = data.body.list;
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages, //返回总页数
                            currentPage: 1
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        clickBtn: function () {
            $('#personDetail').on('click', '.getMoreMsg', function () {
                $('#personDetail .hide').removeClass('hide');
                $(this).addClass('hide');
            });
        },
        detail: function (userName, index) {
            var _this = this;
            var str = {
                userName: userName,
            };
            var userDetail = {
                type: 'post',
                data: str,
                url: '/memberDeposit/depositUserInfo',
                success: function (data) {
                    if (data.code == 200) {
                        if (data.body) {
                            _this.personDetail(data, index);
                        } else {
                            _this.tipsContent("找不到该会员", 1000);
                        }
                    }

                }
            };
            base.sendRequest(userDetail);
        },
        //会员详情展示
        personDetail: function (list, index) {
            var html = "";
            if (list.body) {
                var obj = list.body;
                this.loginTime = this.datas[index].loginTime;
                if (obj.userType == 'NORMAL') {
                    obj.userType = '会员'
                } else if (obj.userType == 'PROBATION') {
                    obj.userType = '试玩'
                }
                html += '<table class="layui-table"><colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup><tbody> ' +
                    '<td style="text-align: right;">\u767B\u5F55\u5E10\u53F7:</td>' +
                    '<td style="text-align: left;">' + (obj.userName ? obj.userName : '-') + '</td>' +
                    '<td style="text-align: right;">\u6240\u5C5E\u4E0A\u7EA7:</td>' +
                    '<td style="text-align: left;">' + (obj.proxyName ? obj.proxyName : '-') + '</td></tr>' +
                    '<tr><td style="text-align: right;">\u4F1A\u5458\u59D3\u540D:</td><td style="text-align: left;">' + (obj.fullName ? obj.fullName : '-') + '</td><td style="text-align: right;">类别:</td><td style="text-align: left;">' + (obj.userType == 1 ? '代理' : '会员') + '</td></tr>' +
                    '<tr class="odd_row"><td style="text-align: right;">\u4F1A\u5458\u4F59\u989D:</td><td style="text-align: left;">' + (obj.coin ? obj.coin : '-') + '</td><td style="text-align: right;">\u79EF\u5206:</td><td style="text-align: left;">' + (obj.score ? obj.score : '0') + '</td></tr>' +
                    '<tr><td style="text-align: right;">\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsum ? obj.betsum : '0.00') + '</td><td style="text-align: right;">\u51FA\u6B3E\u6240\u9700\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsumNeed ? obj.betsumNeed : '0.00') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u72B6\u6001:</td><td style="text-align: left;">' + (obj.status ? obj.status == 2 ? '冻结' : '正常' : '-') + '</td><td style="text-align: right;">\u4F1A\u5458\u7B49\u7EA7:</td><td style="text-align: left;">' + (obj.vipName ? obj.vipName : '-') + '</td></tr><tr class="getMoreMsg" rowspan="2" style="background:#666;color:#fff;cursor:pointer"><td colspan="4"><span>\u5C55\u5F00\u663E\u793A\u66F4\u591A<span></td></tr>' +
                    '<tr class="hide"><td style="text-align: right;">手机号:</td><td style="text-align: left;">' + (obj.phoneNumber ? obj.phoneNumber : '-') + '</td><td style="text-align: right;">\u5FAE\u4FE1\u53F7:</td><td style="text-align: left;">' + (obj.weixin ? obj.weixin : '-') + '</td></tr><tr class="odd_row hide"><td style="text-align: right;">LINE:</td><td style="text-align: left;">' + (obj.line ? obj.line : '-') + '</td><td style="text-align: right;">\u90AE\u7BB1:</td><td style="text-align: left;">' + (obj.email ? obj.email : '-') + '</td></tr><tr><td style="text-align: right;">银行帐号:</td><td colspan="3" style="text-align: left;"><span>' + (obj.bankAccount ? obj.bankAccount : '-') + '</span><span data-text="' + obj.bankAccount + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span><br/><span class="layui-btn layui-btn-mini ' + (obj.bankBlacklistStatus == 0 ? 'layui-btn-danger' : '') + '">' + (obj.bankBlacklistStatus ? obj.bankBlacklistStatus == 1 ? '可用' : '禁用' : '') + '</sapn></td></tr><tr class="odd_row"><td style="text-align: right;">银行名称:</td><td style="text-align: left;">' + (obj.bankName ? obj.bankName : '-') + '</td><td style="text-align: right;">开户分行:</td><td style="text-align: left;">' + (obj.bankAddress ? obj.bankAddress : '-') + '</td></tr><tr><td style="text-align: right;">\u6CE8\u518CIP:</td><td style="text-align: left;">' + (obj.regIp ? obj.regIp : '-') + '</td><td style="text-align: right;">\u6CE8\u518C\u6765\u6E90:</td><td style="text-align: left;">' + (obj.channel ? (obj.channel == 1 ? "pc" : (obj.channel == 2 ? "wap" : (obj.channel == 3 ? "app" : " 管理员添加"))) : '-') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u6CE8\u518C\u7CFB\u7EDF:</td><td style="text-align: left;">' + (obj.regSystem ? obj.regSystem : '-') + '</td><td style="text-align: right;">\u6700\u540E\u767B\u5F55\u65F6\u95F4:</td><td style="text-align: left;">' + (this.loginTime ? this.loginTime : '-') + '</td></tr><tr><td style="text-align: right;">\u5907\u6CE8:</td><td colspan="3" style="text-align: left;">' + (obj.info ? obj.info : '-') + '</td></tr> </tbody></table>';
            } else {
                html = '<p>\u627E\u4E0D\u5230\u8BE5\u4F1A\u5458\u4FE1\u606F</p>';
            }

            $('#personDetail').html(html);
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: '会员详情',
                    area: ['60%', '95%'],
                    content: $('#personDetail'),
                    btn: ['确定'],
                    yes: function yes(index, layero) {
                        layer.closeAll('page');
                    }
                });
            });
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
        //获取搜索里的会员等级名称下拉框的值
        get1: function () {
            let _this = this;
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/user/getMemberList',
                success: function (data) {
                    if (data.code == 200) {
                        _this.grade = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //获取搜索框里的会员筛选条件
        get2: function () {
            let _this = this;
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/user/getKeyList',
                success: function (data) {
                    if (data.code == 200) {
                        _this.names = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
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
        //点击搜索框执行
        search: function () {
            this.startDate = $('#startDate').val();
            this.endDate = $('#endDate').val();
            let _this = this;
            let data = {
                pageIndex: 1,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,

                startTime: this.startDate,
                endTime: this.endDate,
                // username: this.username.trim(),
                username: this.store_username.trim(),
                // loginAccount: this.parentName.trim(),
                loginAccount: this.store_parentName.trim(),
                // vipName: this.vipName,
                vipName: this.store_vipName,
                // keywordOption: this.keywordOption,
                keywordOption: this.store_keywordOption,
                // keywordValue: this.keywordValue.trim(),
                keywordValue: this.store_keywordValue.trim(),
                // rechargeSituation: this.rechargeSituation,
                rechargeSituation: this.store_rechargeSituation,
                // coin: this.coin,
                coin: this.store_coin,
                // loginDays: this.loginDays,
                loginDays: this.store_loginDays,
                parentName: this.store_referee,

                //下面是排序要传的参数，默认是由时间由新到旧排序
                coinSort: this.coinSort,
                timeSort: this.timeSort, //时间由新到旧-默认
                statusSort: this.statusSort,
                onlineSort: this.onlineSort
            };
            this.username = this.store_username.trim();
            // loginAccount: this.parentName.trim(),
            this.parentName = this.store_parentName.trim();
            // vipName: this.vipName,
            this.vipName = this.store_vipName;
            // keywordOption: this.keywordOption,
            this.keywordOption = this.store_keywordOption;
            // keywordValue: this.keywordValue.trim(),
            this.keywordValue = this.store_keywordValue.trim();
            // rechargeSituation: this.rechargeSituation,
            this.rechargeSituation = this.store_rechargeSituation;
            // coin: this.coin,
            this.coin = this.store_coin;
            // loginDays: this.loginDays,
            this.loginDays = this.store_loginDays;
            this.referee = this.store_referee;
            let obj = {
                type: 'post',
                data: data,
                dataType: "json",
                url: '/user/queryUserList',
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg('加载中...', {
                            time: 500
                        });
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        if (data.body.list.length > 0) {
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,
                                currentPage: 1
                            });
                        } else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage: 1
                            });
                        }
                    } else if (data.code == 555) {
                        layer.msg('加载中...', {
                            time: 500
                        });
                        _this.datas = [];
                        _this.total = 0;
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
            if (_this.checkdate(data.startTime, data.endTime)) {
                base.sendRequest(obj);
            }
            // base.sendRequest(obj);
        },
        //点击改变状态
        changeType: function (uid, status, username, onlineStatus, index) {
            let _this = this;
            if (status == 1) {
                status = 2;
            } else {
                status = 1;
            }
            let obj = {
                type: 'post',
                data: {
                    uid: uid,
                    status: status,
                    userName: username
                },
                dataType: "json",
                url: '/user/updateStatc',
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].status = status;
                        layer.msg(data.msg);
                        if (onlineStatus == 1) {
                            _this.force(username);
                        }
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },

        //点击改变管理员状态
        changeAdmin: function (uid, status, username, onlineStatus, index) {
            let _this = this;
            if (status == 1) {
                status = 0;
            } else {
                status = 1;
            }
            let data = {
                uid: uid,
                CHAT_ADMIN: status
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/user/updateStatc',
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].chat_ADMIN = status;
                        layer.msg(data.msg);
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);

        },

        //改变状态时强制会员下线
        force(username) {
            let data = {
                userName: username
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/forcedOffline',
                success: function (data) {
                    //console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击会员名弹出个人信息
//		detail: function (obj) {
//			this.obj = obj;
//			////console.log(obj);
//			layer.open({
//				title: '查看会员详细信息',
//				type: 1,
//				content: $('.detail'),
//				area: ['60%', '80%'],
//				btn: ['关闭'],
//				yes: function (index) {
//					layer.close(index);
//				}
//			})
//		},
        //修改会员框键盘抬起检测是否有此代理
        check_proxy() {
            // let _this = this;
            // let obj = {
            // 	type: 'get',
            // 	data: {
            // 		proxyName: this.daili
            // 	},
            // 	dataType: 'json',
            // 	url: '/user/getProxyList',
            // 	success: function (data) {
            // 		if (data.code == 200) {
            // 			_this.dailis_ = data.body;
            // 			$('.check_2').show();
            // 		}else{
            //                   _this.dailis_ = [];
            // 		}
            // 	},
            // 	error: function (msg) {
            // 		//console.log(msg);
            // 	}
            // };
            // base.sendRequest(obj);
        },
        //修改会员框代理框失焦时隐藏代理列表
        hide_() {
            setTimeout(function () {
                $('.check_2').hide();
            }, 200);
        },
        //修改会员框选择某一代理执行
        select_proxy(id, name) {
            this.daili = '';
            this.daili = name;
            this.proxy_id = id;
            $('.check_2').hide();
        },
        //修改会员框获取代理数组
        get_proxy() {
            let _this = this;
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/proxyinfo/selectProxyInfoAll',
                success(data) {
                    if (data.code == 200) {
                        _this.all_proxy = data.body;
                    }
                },
                error(msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击修改会员信息
        edit: function (obj) {
            this.get_proxy();
            if (obj.bankName == undefined) {
                this.rank = '';
            } else {
                this.rank = obj.bankName;
            }
            if (obj.bankAddress == undefined) {
                this.rank_address = '';
            } else {
                this.rank_address = obj.bankAddress;
            }
            if (obj.line == undefined) {
                this.line = '';
            } else {
                this.line = obj.line;
            }
            if (obj.weixin == undefined) {
                this.weixin = '';
            } else {
                this.weixin = obj.weixin;
            }
            if (obj.email == undefined) {
                this.email = '';
            } else {
                this.email = obj.email;
            }
            if (obj.fullName == undefined) {
                this.name = '';
            } else {
                this.name = obj.fullName;
            }
            if (obj.phoneNumber == undefined) {
                this.iphone = '';
            } else {
                this.iphone = obj.phoneNumber;
            }
            if (obj.bankAccount == undefined) {
                this.rank_hao = '';
            } else {
                this.rank_hao = obj.bankAccount;
            }
            if (obj.remark == undefined) {
                this.remark = '';
            } else {
                this.remark = obj.remark;
            }
            this.uid = obj.uid;
            this.zhanghao = obj.username;
            this.daili = obj.proxyName;
            this.vip = obj.vipId;
            this.rank_black = obj.bankBlackListStatus;
            this.state = obj.status;
            this.reg_ip = obj.regIp;
            this.channel = obj.channel;
            this.system = obj.regSystem;
            this.parents = obj.proxyName;
            let _this = this;
            layer.open({
                title: '修改会员信息',
                type: 1,
                content: $('.associator_msg'),
                area: ['60%', '80%'],
                btn: ['保存', '关闭'],
                yes: function (index) {
                    /*if (_this.iphone != '') {
                        if (_this.flag_tel == false) {
                            layer.msg('手机号码格式错误');
                            return;
                        }
                    }
                    if (_this.qq.trim() != '') {
                        if (!(/[1-9]([0-9]{5,16})/.test(_this.qq))) {
                            layer.msg('QQ号码格式错误');
                            return;
                        }
                    }
                    if (_this.weixin.trim() != '') {
                        if (!(/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$/.test(_this.weixin))) {
                            layer.msg('微信号格式错误');
                            return;
                        }
                    }
                    if (_this.email != '') {
                        if (_this.flag_email == false) {
                            layer.msg('邮箱格式错误');
                            return;
                        }
                    }
                    if (_this.rank_hao.trim() != '') {
                        if (!(/^([1-9]{1})(\d{14}|\d{18})$/.test(_this.rank_hao))) {
                            layer.msg('银行帐号格式错误');
                            return;
                        }
                    }
                    if (_this.name.trim().length > 10) {
                        layer.msg('会员姓名格式错误(2-10位)');
                        return;
                    }*/
                    // if (_this.daili != '') {
                    // 	for (let i = 0; i < _this.all_proxy.length; i++) {
                    // 		if (_this.daili == _this.all_proxy[i].parentName) {
                    // 			_this.proxy_id = _this.all_proxy[i].pid;
                    // 			_this.flag_ = true;
                    // 		}
                    // 	}
                    // } else {
                    _this.flag_ = true;
                    // }
                    if (_this.rank.length != 0) {
                        _this.rank = _this.rank.trim();
                    }
                    if (_this.rank_address.length != 0) {
                        _this.rank_address = _this.rank_address.trim();
                    }
                    let data = {
                        uid: _this.uid,
                        username: _this.zhanghao,
                        // proxyName: _this.daili,
                        // proxyId: _this.proxy_id,
                        fullName: _this.name,
                        phoneNumber: _this.iphone,
                        email: _this.email,
                        bankName: _this.rank,
                        bankAddress: _this.rank_address,
                        weixin: _this.weixin,
                        line: _this.line,
                        bankAccount: _this.rank_hao,
                        vipId: _this.vip,
                        bankBlackListStatus: _this.rank_black,
                        status: _this.state,
                        regIp: _this.reg_ip,
                        source: _this.channel,
                        regSystem: _this.system,
                        parentName: _this.parents,
                        remark: _this.remark
                    };
                    if (_this.flag_) {
                        let obj = {
                            type: 'post',
                            data: data,
                            dataType: 'json',
                            url: '/user/updateVipUser',
                            success: function (data) {
                                if (data.code == 200) {
                                    layer.close(index);
                                    layer.msg(data.msg);
                                    setTimeout(function () {
                                        window.location.reload();
                                    }, 1000);
                                } else {
                                    layer.msg(data.msg);
                                }
                            },
                            error: function (msg) {
                                //console.log(msg);
                            }
                        };
                        base.sendRequest(obj);
                    } else {
                        layer.msg('代理不存在');
                    }
                },
                btn2: function () {
                }
            });
        },
        //检测修改会员手机号变更时格式是否正确
        check_iphone() {
            if (this.iphone != '') {
                let a = /^09\d{8}$/;
                let b = this.iphone.match(a);
                if (b == null) {
                    layer.msg('手机号码格式错误');
                    this.flag_tel = false;
                } else {
                    layer.msg('手机号码格式正确');
                    this.flag_tel = true;
                }
            }
        },
        //检测修改会员邮箱变更时格式是否正确
        check_email() {
            if (this.email != '') {
                let a = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,20}/;
                let b = this.email.match(a);
                if (b == null) {
                    layer.msg('邮箱格式错误');
                    this.flag_email = false;
                } else {
                    layer.msg('邮箱格式正确');
                    this.flag_email = true;
                }
            }
        },
        //点击修改会员登录密码
        editPass: function (obj) {
            this.pssword = '';
            this.rePssword = '';
            this.uid1 = obj.uid;
            this.zhanghao1 = obj.username;
            let _this = this;
            layer.open({
                title: '修改会员登录密码',
                type: 1,
                content: $('.edit_pass'),
                area: ['400px', '280px'],
                btn: ['保存', '关闭'],
                yes: function (index) {
                    if (_this.pssword.trim().length < 6 || _this.pssword.trim().length > 12) {
                        layer.msg('密码长度6-12位');
                        return;
                    }
                    if (_this.pssword.trim() != _this.rePssword.trim()) {
                        layer.msg('两次密码输入不一致');
                        return;
                    }
                    let md5 = hex_md5(_this.zhanghao1 + _this.pssword);
                    let md5_ = hex_md5(_this.zhanghao1 + _this.rePssword);
                    let data = {
                        uid: _this.uid1,
                        password: md5,
                        rePassword: md5_
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/user/updateVipUser1',
                        success: function (data) {
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                if (obj.onlineStatus == 1) {
                                    _this.force(_this.zhanghao1);
                                }
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1000);
                                _this.downline();
                            } else {
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest(obj);
                },
                btn2: function () {
                }
            })
        },
        downline() {
            let data = {
                userName: this.zhanghao1,
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/forcedOffline',
                success: function (data) {
                    layer.msg("用户已被下线");
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击修改会员资金密码
        editMoney_Pass(obj) {
            this.pssword3 = '';
            this.rePssword3 = '';
            this.uid3 = obj.uid;
            this.zhanghao3 = obj.username;
            let _this = this;
            layer.open({
                title: '修改会员资金密码',
                type: 1,
                content: $('.edit_Money_pass'),
                area: ['400px', '280px'],
                btn: ['保存', '关闭'],
                yes: function (index) {
                    if (_this.pssword3.trim().length < 6 || _this.pssword3.trim().length > 12) {
                        layer.msg('密码长度6-12位');
                        return;
                    }
                    if (_this.pssword3.trim() != _this.rePssword3.trim()) {
                        layer.msg('两次密码输入不一致');
                        return;
                    }
                    let md5 = hex_md5(_this.zhanghao3 + _this.pssword3);
                    let md5_ = hex_md5(_this.zhanghao3 + _this.rePssword3);
                    let data = {
                        uid: _this.uid3,
                        coinPssword: md5,
                        reCoinPssword: md5_
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/user/updateVipUser1',
                        success: function (data) {
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest(obj);
                }
            })
        },
        //帐号失焦检验是否存在
        check_username: function () {
            if (this.zhanghao2.trim().length < 5 || this.zhanghao2.trim().length > 11 || this.zhanghao2.trim() === '') {
                layer.msg('格式错误！英文、数字5-11位');
                return;
            }
            let a = /[\u4e00-\u9fa5]/;
            let b = this.zhanghao2.match(a);
            if (b) {
                layer.msg('格式错误，不能包含中文!');
                return;
            }
            let _this = this;
            let obj = {
                type: 'get',
                data: {
                    username: this.zhanghao2
                },
                dataType: 'json',
                url: '/user/checkUsername',
                success: function (data) {
                    if (data.code == 200) {
                        _this.flag1 = true;
                        layer.msg(data.msg);
                    } else {
                        _this.flag1 = false;
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //添加会员框键盘抬起检测是否有此代理
        check_daili: function () {
            let _this = this;
            let obj = {
                type: 'get',
                data: {
                    proxyName: this.daili2
                },
                dataType: 'json',
                url: '/user/getProxyList',
                success: function (data) {
                    if (data.code == 200) {
                        _this.dailis = data.body;
                        $('.check').show();
                    } else {
                        _this.dailis = [];
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //添加会员框代理框失焦时隐藏代理列表
        hide: function () {
            var _this = this;
            //隐藏显示框
            setTimeout(function () {
                $('.check').hide();
            }, 200);
            //emptyRebateGameList
            if (!_this.daili2) {
                _this.rebateConfigList = "";
                _this.rebateConfigList = _this.emptyRebateGameList;
            }

        },
        //添加会员框选择某一代理执行
        select_daili: function (id, name, index) {
            this.daili2 = '';
            this.daili2 = name;
            this.daili_id = id;
            //ssc#4@k3#4@11x5#4@3D#4@PK10#4@hk6#4@7xc#4@kl10f#4@PCDD#4
            var rebatedata = this.dailis[index].data,
                codeNameList = [], _this = this;
            rebatedata = rebatedata.split("@");
            rebatedata.map(function (item, index) {
                rebatedata[index] = item.split("#");
                codeNameList.push(rebatedata[index][0]);
            })

            _this.rebateConfigList.map(function (item, sIndex) {
                var oIndex = codeNameList.indexOf(item.code);
                if (oIndex != -1) {
                    _this.rebateConfigList[sIndex].rebate = rebatedata[oIndex][1];
                }
            })
            $('.check').hide();
        },

        //检测新增会员手机格式输入是否正确
        check_iphone2() {
            let a = /^09\d{8}$/;
            let b = this.iphone2.match(a);
            if (b == null) {
                layer.msg('手机号码格式错误');
                this.flag_tel2 = false;
            } else {
                layer.msg('手机号码格式正确');
                this.flag_tel2 = true;
            }
        },
        //检测新增会员返点设置是否正确
        check_rebate(index) {
            var _this = this, val = _this.rebateGameList[index], maxVal = _this.rebateConfigList[index].rebate;
            if (isNaN(parseInt(val))) {
                layer.msg(_this.rebateConfigList[index].codeName + '的返点格式错误(应为浮点数)');
                return 1
            } else if (parseFloat(val) < 0) {
                layer.msg(_this.rebateConfigList[index].codeName + '返点最小不能小于0');
                _this.$set(_this.rebateGameList, index, 0);
                return 1
            } else if (parseFloat(val) > parseFloat(maxVal)) {
                layer.msg(_this.rebateConfigList[index].codeName + '返点最大不能超过' + maxVal);
                _this.$set(_this.rebateGameList, index, maxVal);
                return 1
            } else {
                val = parseFloat(val).toFixed(1);
                _this.$set(_this.rebateGameList, index, val);
            }
            return 0
            // //console.log(rebate);
            //ssc#4@k3#4@11x5#4@3D#4@PK10#4@hk6#4@7xc#4@kl10f#4@PCDD#4
            //ssc#7.8@k3#8.3@11x5#7.3@3D#7.3@PK10#7.9@hk6#9.8@7xc#7.8@kl10f#8.3@PCDD#8.3
        },
        //点击新增会员
        add: function () {
            let pc_app = browserType.initMethod();
            this.regSystem = pc_app.channel;
            $('.check').hide();
            this.zhanghao2 = '';
            this.daili2 = '';
            this.password2 = '';
            this.password2_ = '';
            this.name2 = '';
            this.iphone2 = '';
            this.email2 = '';
            this.line2 = '';
            this.weixin2 = '';
            this.rank2 = '';
            this.rank_hao2 = '';
            this.rank_address2 = '';
            this.vip_ = 0;
            this.state_ = 1;
            this.daili_id = '';

            let _this = this;
            layer.open({
                title: '添加会员',
                type: 1,
                content: $('.add'),
                area: ['60%', '80%'],
                btn: ['保存', '关闭'],
                yes: function (index) {
                    _this.flag = false;
                    if (_this.zhanghao2.trim() == '') {
                        layer.msg('帐号不能为空');
                        return;
                    }
                    if (_this.password2.trim() == '') {
                        layer.msg('密码不能为空');
                        return;
                    }
                    if (_this.password2.trim().length < 6 || _this.password2.trim().length > 16) {
                        layer.msg('密码长度6-16位');
                        return;
                    }
                    if (_this.password2_.trim() != _this.password2.trim()) {
                        layer.msg('两次密码输入不一致');
                        return;
                    }
                    if (_this.name2.trim() === '' || _this.name2.trim().length < 2 || _this.name2.trim().length > 10) {
                        layer.msg('会员姓名不能为空或格式错误(2-10位)');
                        return;
                    }
                    if (_this.iphone2.trim() == '' || _this.flag_tel2 == false) {
                        layer.msg('手机号码格式错误');
                        return;
                    }
//                    if (_this.qq2.trim() != '') {
//                        if (!(/[1-9]([0-9]{5,16})/.test(_this.qq2))) {
//                            layer.msg('QQ号码格式错误');
//                            return;
//                        }
//                    }
                    /*if (_this.weixin2.trim() != '') {
                        if (!(/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$/.test(_this.weixin2))) {
                            layer.msg('微信号格式错误');
                            return;
                        }
                    }*/
                    if (_this.email2.trim() != '') {
                        if (!(/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,20}/.test(_this.email2))) {
                            layer.msg('邮箱格式错误');
                            return;
                        }
                    }
                    /*if(_this.rank_hao2.trim() != ''){
                        if(!(/^([1-9]{1})(\d{14}|\d{18})$/.test(_this.rank_hao2))){
                            layer.msg('银行帐号格式错误');
                            return;
                        }
                    }*/
                    if (_this.daili2 != '') {
                        for (let i = 0; i < _this.dailis.length; i++) {
                            if (_this.daili2 != _this.dailis[i].name) {
                                continue;
                            } else {
                                _this.daili_id = _this.dailis[i].id;
                                _this.flag = true;
                                break;
                            }
                        }
                    } else {
                        _this.flag = true;
                    }
                    var passwordLevel;
                    var middle = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
                    var top = /^(?!([a-zA-Z\d]*|[\d!@#\$%_\.]*|[a-zA-Z!@#\$%_\.]*)$)[a-zA-Z\d!@#\$%_\.]{6,16}/;
                    if (top.test(_this.password2)) {
                        passwordLevel = 3;
                    } else if (middle.test(_this.password2)) {
                        passwordLevel = 2;
                    } else {
                        passwordLevel = 1;
                    }
                    let md5 = hex_md5(_this.zhanghao2.trim() + _this.password2.trim());
                    let md5_ = hex_md5(_this.zhanghao2.trim() + _this.password2_.trim());
                    // if(_this.userType!=1){
                    var rNum = 0;
                    for (var i = 0, len = _this.rebateGameList.length; i < len; i++) {
                        rNum = _this.check_rebate(i);
                        if (rNum == 1) {
                            break;
                        }
                    }
                    if (rNum == 1) {
                        return
                    }
                    //返点设置
                    var rebate = "";
                    _this.rebateGameList.map(function (item, index) {
                        item = parseFloat(item).toFixed(1);
                        if (!rebate) {
                            rebate += _this.rebateConfigList[index].code + "#" + item;
                        } else {
                            rebate += "@" + _this.rebateConfigList[index].code + "#" + item;
                        }
                    })
                    // }

                    let data = {
                        data: rebate,
                        userType: parseInt(_this.userType) + 1,
                        username: _this.zhanghao2.trim(),
                        password: md5,
                        rePassword: md5_,
                        fullName: _this.name2.trim(),
                        phoneNumber: _this.iphone2.trim(),
                        email: _this.email2.trim(),
                        line: _this.line2.trim(),
                        weixin: _this.weixin2.trim(),
                        bankName: _this.rank2.trim(),
                        bankAccount: _this.rank_hao2.trim(),
                        bankAddress: _this.rank_address2.trim(),
                        vipId: _this.vip_,
                        status: _this.state_,
                        proxyId: _this.daili_id?parseInt(_this.daili_id):'',
                        proxyName: _this.daili2,
                        regSystem: _this.regSystem,
                        passwordLevel: passwordLevel,
                    };
                    if (_this.flag == true && _this.flag1 == true) {
                        let obj = {
                            type: 'post',
                            data: data,
                            dataType: 'json',
                            url: '/user/insertVipUser',
                            success: function (data) {
                                if (data.code == 200) {
                                    layer.close(index);
                                    layer.msg(data.msg);
                                    setTimeout(function () {
                                        window.location.reload();
                                    }, 1000);
                                } else {
                                    layer.msg(data.msg);
                                }
                            },
                            error: function (msg) {
                                //console.log(msg);
                            }
                        };
                        base.sendRequest(obj);
                    } else if (_this.flag1 == false) {
                        layer.msg('帐号已存在或格式错误');
                    } else {
                        layer.msg('代理不存在');
                    }
                },
                btn2: function () {
                }
            })
        },
        //点击解锁
        unlocked: function (username) {
            let data = {
                username: username
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/user/unlockMember',
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg(data.msg);
                        setTimeout(function () {
                            window.location.reload();
                        }, 500)
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },

        export1: function () {
            $('#form_').submit();
        },
        /*------------------------------------下面是点击排序方法-----------------------------*/
        //按金额由小到大排序
        coin_sort0: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#coin_sort0').css('border-bottom-color', '#000000');
            this.coinSort = 1;
            this.timeSort = '';
            this.statusSort = '';
            this.onlineSort = '';
            this.getdatas(1);
        },
        //按金额由大到小排序
        coin_sort1: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#coin_sort1').css('border-top-color', '#000000');
            this.coinSort = 2;
            this.timeSort = '';
            this.statusSort = '';
            this.onlineSort = '';
            this.getdatas(1);
        },
        //按时间由旧到新排序
        time_sort0: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#time_sort0').css('border-bottom-color', '#000000');
            this.coinSort = '';
            this.timeSort = 1;
            this.statusSort = '';
            this.onlineSort = '';
            this.getdatas(1);
        },
        //按时间新到旧排序
        time_sort1: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#time_sort1').css('border-top-color', '#000000');
            this.coinSort = '';
            this.timeSort = 2;
            this.statusSort = '';
            this.onlineSort = '';
            this.getdatas(1);
        },
        //按在线状态排序0是离线
        online_sort0: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#online_sort0').css('border-bottom-color', '#000000');
            this.coinSort = '';
            this.timeSort = '';
            this.statusSort = '';
            this.onlineSort = 1;
            this.getdatas(1);
        },
        //按在线状态排序1是离线
        online_sort1: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#online_sort1').css('border-top-color', '#000000');
            this.coinSort = '';
            this.timeSort = '';
            this.statusSort = '';
            this.onlineSort = 2;
            this.getdatas(1);
        },
        //按会员状态排序0是禁用
        state_sort0: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#state_sort0').css('border-bottom-color', '#000000');
            this.coinSort = '';
            this.timeSort = '';
            this.statusSort = 1;
            this.onlineSort = '';
            this.getdatas(1);
        },
        //按会员状态排序1是启用
        state_sort1: function () {
            $('.changecolor').css({
                'border-top-color': '#b2b2b2',
                'border-bottom-color': '#b2b2b2'
            });
            $('#state_sort1').css('border-top-color', '#000000');
            this.coinSort = '';
            this.timeSort = '';
            this.statusSort = 2;
            this.onlineSort = '';
            this.getdatas(1);
        },
        //点击跳转到帐变
        href1: function (username) {
            let zh = {
                username: username,
                type: 1
            };
            zh = JSON.stringify(zh);
            sessionStorage.zhanghao = zh;
            parent.test();
        },
        //点击跳转到报表
        href2: function (username) {
            sessionStorage.baobiao = username;
            parent.associator_2();
        },
        //点击跳转到财务
        href3: function (username) {
            sessionStorage.pinggu = username;
            parent.associator_3();
        },
        //点击跳转到概况
        href4: function (username) {
            sessionStorage.gaikuang = username;
            parent.associator_4();
        },
        // 检测时间的先后顺序
        checkdate: function (start, end) {
            //得到日期值并转化成日期格式，replace(//-/g, "//")是根据验证表达式把日期转化成长日期格式，这样
            //再进行判断就好判断了
            var sDate = new Date(start.replace(/\-/g, "\/"));
            var eDate = new Date(end.replace(/\-/g, "\/"));
            if (sDate > eDate) {
                layer.alert('结束时间不能早于开始时间', {
                    skin: 'layui-layer-molv',
                    closeBtn: 0,
                    anim: 4 //动画类型
                });
                return false;
            }
            return true;
        }
    },
    watch: {
        //监听下拉框的值(每页显示多少条数据)
        pageNum: function () {
            this.getdatas(1);
        },
        topStatus(val){
            if (val == 1){
                this.loadList();
            }
        },
        userType(val){
            if(this.topStatus==1){
                this.loadList();
            }
            // //console.log(val)
        },
        iphone:function(){
            this.iphone = this.iphone.replace(/\D+/g, '');
        },
        iphone2:function(){
            this.iphone2 = this.iphone2.replace(/\D+/g, '');
        },
        rank_hao:function(){
            this.rank_hao = this.rank_hao.replace(/\D+/g, '');
        },
        rank_hao2:function(){
            this.rank_hao2 = this.rank_hao2.replace(/\D+/g, '');
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
        app.getdatas(num);
        app.loadList(num);
    }
});

// 加载分页功能
$.jqPaginator('#pages', {
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
        app.loadList(num);
    }
});
