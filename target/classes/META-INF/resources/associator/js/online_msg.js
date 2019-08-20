/**
 * Created by ASUS on 2017/10/21.
 */
let app = new Vue({
    el: '#app',
    data: {
        datas: [],        //数据列表
        pageNum: 50,      //默认每页5条数据
        total: '',       //总条数

        search_input: '',    //绑定搜索框
        type_: 'name',       //默认搜索条件

        grade: [],       //会员等级数组
        //修改会员信息绑定要传的参数
        uid: '',             //uid
        zhanghao: '',        //登录帐号
        daili: '',           //指定代理
        datli_id:'',         //代理id
        name: '',            //会员姓名
        iphone: '',          //手机号
        weixin: '',          //微信
        email: '',           //邮箱
        LINE: '',              //line
        rank: '',            //银行帐号
        rank_hao: '',        //银行帐号
        rank_address: '',    //开户分行
        remark: '',          //备注
        vip: '',             //会员等级-下拉
        rank_black: '',      //银行卡黑名单-下拉
        state: '',           //会员状态-下拉
        reg_ip: '',          //注册ip
        channel: '',            //注册来源
        system: '',          //注册系统
        parents: '',         //所属上级

        obj: {},         //个人详情数据
    },
    created: function () {
        this.$nextTick(function () {
            this.get1();
            this.getlayer();
            Date.prototype.toLocaleString = function () {       //此方法是改变toLocaleString的方法，换成想要的时间格式。
                return this.getFullYear() + "-" + ((this.getMonth() + 1) > 9 ? (this.getMonth() + 1) : ('0' + (this.getMonth() + 1))) + "-" + (this.getDate() > 9 ? (this.getDate()) : ('0' + this.getDate())) + " " + this.getHours() + ":" + this.getMinutes() + ":" + this.getSeconds();
            };
        })
    },
    mounted:function(){
    	this.clickBtn();
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use('layer', function () {
                let layer = layui.layer;
            })
        },
        //获取在线列表
        getdatas: function (num) {
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize:5,
                userName: localStorage.userName,
                // pageSize: 10
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/getOnlineUserList',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        let arr = [];
                        arr = data.body.userInfoList;
                        _this.total = data.body.pageSize;
//                      for (let i = 0; i < arr.length; i++) {
//                          arr[i] = JSON.parse(arr[i]);
//                      }
                        _this.datas = arr;
                        // //console.log(arr);
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
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        clickBtn:function(){
			$('#personDetail').on('click', '.getMoreMsg', function() {
				$('#personDetail .hide').removeClass('hide');
				$(this).addClass('hide');
			});
		},
		detail:function(userName){
			var _this = this;
			var str = {
					userName: userName,
				};
				var userDetail = {
					type: 'post',
					data: str,
					url: '/memberDeposit/depositUserInfo',
					success: function (data) {
						if(data.code==200){
							if(data.body){
								_this.personDetail(data);
							}else{
								_this.tipsContent("找不到该会员",1000);
							}
						}
						
					}
				};
				base.sendRequest(userDetail);
		},
		//会员详情展示
		personDetail: function(list) {
			var html = "";
			if(list.body) {
				var obj = list.body;
				if(obj.userType == 'NORMAL') {
					obj.userType = '会员'
				} else if(obj.userType == 'PROBATION') {
					obj.userType = '试玩'
				}
                html += '<table class="layui-table"><colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup><tbody> <td style="text-align: right;">\u767B\u5F55\u5E10\u53F7:</td><td style="text-align: left;">' + (obj.userName ? obj.userName : '-') + '</td><td style="text-align: right;">\u6240\u5C5E\u4E0A\u7EA7:</td><td style="text-align: left;">' + (obj.proxyName ? obj.proxyName : '-') + '</td></tr><tr><td style="text-align: right;">\u4F1A\u5458\u59D3\u540D:</td><td style="text-align: left;">' + (obj.fullName ? obj.fullName : '-') + '</td><td style="text-align: right;">类别:</td><td style="text-align: left;">' + (obj.userType == 1 ? '代理' : '会员') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u4F1A\u5458\u4F59\u989D:</td><td style="text-align: left;">' + (obj.coin ? obj.coin : '-') + '</td><td style="text-align: right;">\u79EF\u5206:</td><td style="text-align: left;">' + (obj.score ? obj.score : '0') + '</td></tr><tr><td style="text-align: right;">\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsum ? obj.betsum : '0.00') + '</td><td style="text-align: right;">\u51FA\u6B3E\u6240\u9700\u6253\u7801\u91CF:</td><td style="text-align: left;">' + (obj.betsumNeed ? obj.betsumNeed : '0.00') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u72B6\u6001:</td><td style="text-align: left;">' + (obj.status ? obj.status == 2 ? '冻结' : '正常' : '-') + '</td><td style="text-align: right;">\u4F1A\u5458\u7B49\u7EA7:</td><td style="text-align: left;">' + (obj.vipName ? obj.vipName : '-') + '</td></tr><tr class="getMoreMsg" rowspan="2" style="background:#666;color:#fff;cursor:pointer"><td colspan="4"><span>\u5C55\u5F00\u663E\u793A\u66F4\u591A<span></td></tr><tr class="hide"><td style="text-align: right;">手机号:</td><td style="text-align: left;">' + (obj.phoneNumber ? obj.phoneNumber : '-') + '</td><td style="text-align: right;">\u5FAE\u4FE1\u53F7:</td><td style="text-align: left;">' + (obj.weixin ? obj.weixin : '-') + '</td></tr><tr class="odd_row hide"><td style="text-align: right;">LINE:</td><td style="text-align: left;">' + (obj.line ? obj.line : '-') + '</td><td style="text-align: right;">\u90AE\u7BB1:</td><td style="text-align: left;">' + (obj.email ? obj.email : '-') + '</td></tr><tr><td style="text-align: right;">银行帐号:</td><td colspan="3" style="text-align: left;"><span>' + (obj.bankAccount ? obj.bankAccount : '-') + '</span><span data-text="' + obj.bankAccount + '" class="copyBtn layui-btn layui-btn-mini">\u590D\u5236</span><br/><span class="layui-btn layui-btn-mini ' + (obj.bankBlacklistStatus == 0 ? 'layui-btn-danger' : '') + '">' + (obj.bankBlacklistStatus ? obj.bankBlacklistStatus == 1 ? '可用' : '禁用' : '') + '</sapn></td></tr><tr class="odd_row"><td style="text-align: right;">银行名称:</td><td style="text-align: left;">' + (obj.bankName ? obj.bankName : '-') + '</td><td style="text-align: right;">开户分行:</td><td style="text-align: left;">' + (obj.bankAddress ? obj.bankAddress : '-') + '</td></tr><tr><td style="text-align: right;">\u6CE8\u518CIP:</td><td style="text-align: left;">' + (obj.regIp ? obj.regIp : '-') + '</td><td style="text-align: right;">\u6CE8\u518C\u6765\u6E90:</td><td style="text-align: left;">' + (obj.channel ? (obj.channel == 1 ? "pc" : (obj.channel == 2 ? "wap" : (obj.channel == 3 ? "app" : " 管理员添加"))) : '-') + '</td></tr><tr class="odd_row"><td style="text-align: right;">\u6CE8\u518C\u7CFB\u7EDF:</td><td style="text-align: left;">' + (obj.regSystem ? obj.regSystem : '-') + '</td><td style="text-align: right;">\u6700\u540E\u767B\u5F55\u65F6\u95F4:</td><td style="text-align: left;">' + (obj.lastLoginTime ? obj.lastLoginTime : '-') + '</td></tr><tr><td style="text-align: right;">\u5907\u6CE8:</td><td colspan="3" style="text-align: left;">' + (obj.info ? obj.info : '-') + '</td></tr> </tbody></table>';
			} else {
				html = '<p>\u627E\u4E0D\u5230\u8BE5\u4F1A\u5458\u4FE1\u606F</p>';
			}

			$('#personDetail').html(html);
			layui.use('layer', function () {
				var layer = layui.layer;
				layer.open({
					type: 1,
					title: '会员详情',
					area: ['60%', '90%'],
					content: $('#personDetail'),
					btn: ['确定'],
					yes: function yes(index, layero) {
						layer.closeAll('page');
					}
				});
			});
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
                    ////console.log(data);
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
        //点击修改
        edit: function (obj) {
            ////console.log(obj);
            if(obj.NAME == undefined){
                this.name = '';
            }else {
                this.name = obj.NAME;
            }
            if(obj.PHONE_NUMBER == undefined){
                this.iphone = '';
            }else {
                this.iphone = obj.PHONE_NUMBER;
            }
            if(obj.WEIXIN == undefined){
                this.weixin = '';
            }else {
                this.weixin = obj.WEIXIN;
            }
            if(obj.EMAIL == undefined){
                this.email = '';
            }else {
                this.email = obj.EMAIL;
            }
            if(obj.line == undefined){
                this.line = '';
            }else {
                this.line = obj.line;
            }
            if(obj.BANK_NAME == undefined){
                this.rank = '';
            }else {
                this.rank = obj.BANK_NAME;
            }
            if(obj.BANK_ADDRESS == undefined){
                this.rank_address = '';
            }else {
                this.rank_address = obj.BANK_ADDRESS;
            }
            if(obj.BANK_ACCOUNT == undefined){
                this.rank_hao = '';
            }else {
                this.rank_hao = obj.BANK_ACCOUNT;
            }
            if(obj.REMARK == undefined){
                this.remark = '';
            }else {
                this.remark = obj.REMARK;
            }
            this.uid = obj.UID;
            this.zhanghao = obj.USER_NAME;
            this.daili = obj.AGENT_COUNT;
            this.datli_id = obj.AGENT_ID;
            this.vip = obj.VIP_ID;
            this.rank_black = obj.BANK_BLACKLIST_STATUS;
            this.state = obj.STATUS;
            this.reg_ip = obj.REG_IP;
            this.channel = obj.CHANNEL;
            this.system = obj.REG_SYSTEM;
            this.parents = obj.AGENT_COUNT;
            let _this = this;
            layer.open({
                title: '修改会员信息',
                type: 1,
                content: $('.associator_msg'),
                area: ['52%'],
                btn: ['保存', '关闭'],
                yes: function (index) {
                    let data = {
                        uid: _this.uid,
                        username: _this.zhanghao,
                        proxyName: _this.daili,
                        proxyId:parseInt(_this.datli_id),
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
                    //console.log(data);
                    if (_this.iphone.trim().length > 11) {
                        layer.msg('手机号号码必须是11位！');
                        return;
                    }
                    if(_this.name.trim().length>10){
                        layer.msg('会员姓名格式错误(2-10位)');
                        return;
                    }
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/user/updateVipUser',
                        success: function (data) {
                            ////console.log(data);
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
                },
                btn2: function () {
                }
            });
        },
        //点击强制下线
        force: function (username) {
            layer.open({
                title: '信息',
                type: 1,
                content: $('.force'),
                area: ['21%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    let data = {
                        userName: username
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/forcedOffline',
                        success: function (data) {
                            ////console.log(data);
                            layer.close(index);
                            layer.msg(data.msg);
                            setTimeout(function () {
                                window.location.reload();
                            }, 1000);
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
        //点击搜索执行
        search: function () {
            let _this = this;
            let data = {
                pageIndex: 1,
                pageNum: parseInt(this.pageNum),
                userName: this.search_input.trim(),
                type: this.type_,
                pageSize:5,
                // pageSize: 10
            };
            // //console.log(data);
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/getOnlineUserList',
                success: function (data) {
                    ////console.log(data);
                    layer.msg('加载中...', {time: 500});
                    if (data.code == 200) {
                    	_this.total = data.body.pageSize;
                        let arr = [];
                        arr = data.body.userInfoList;
//                      for (let i = 0; i < arr.length; i++) {
//                          arr[i] = JSON.parse(arr[i]);
//                      }
                        _this.datas = arr;
                        // //console.log(arr);
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages,
                            currentPage: 1
                        });
                    } else {
                        _this.datas = [];
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
            base.sendRequest(obj);
        },
        //点击会员名弹出个人信息
//      detail: function (username) {
//          let data = {
//              userName: username
//          };
//          let _this = this;
//          let obj = {
//              type: 'get',
//              data: data,
//              dataType: 'json',
//              url: '/user/getUserByUserName',
//              success(data) {
//                  // //console.log(data);
//                  if(data.code==200){
//                      _this.obj = data.body;
//                  }
//              },
//              error(msg) {
//                  //console.log(msg);
//              }
//          };
//          base.sendRequest(obj);
//          layer.open({
//              title: '查看会员详细信息',
//              type: 1,
//              content: $('.detail'),
//              area: ['60%','80%'],
//              btn: ['关闭'],
//              yes: function (index) {
//                  layer.close(index);
//              }
//          })
//      }
    },
    watch:{
    	pageNum:function(){
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