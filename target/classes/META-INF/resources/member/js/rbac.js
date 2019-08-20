var app = new Vue({
    el: '#app',
    data: {
        datas: [],//用户列表
        search_input: '',//绑定搜索框
        page_num: 50, //分页中每页显示多少条数据
        select: '',  //变更角色框中下拉框的值
        total: '',   //信息总条数

        info: [],    //下拉框内容
        select_: '',  //添加管理员监听下拉框的值
        name: '',    //添加管理员用户名
        prompt3: '',  //验证用户名是否可用的提示
        flag: false,  //验证用户名控制变量
        ChName: '',  //中文用户名
        pass: '',    //密码

        userChName: '',//变更角色中的当前角色

        userid: '',  //贮存变更ID
        //chName: '',   //贮存中文名
        //oddpass: '',  //旧密码
        //flag1: false, //旧密码控制变量
        prompt4: '',  //验证旧密码是否正确的提示
        newpass: '', //新密码
        newpass_: '',//再次确定新密码
        username: '',//贮存用户名
        prompt: '',  //密码提示
        prompt1: '', //变更角色提示
        store_search:'',
    },
    created: function () {
        this.getlayer();
        this.get_info();
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use('layer', function () {
                let layer = layui.layer;
            });
        },
        // 获取列表
        getdatas: function (num) {
            let _this = this;
            let options = {
                type: "get",
                data: {
                    pageIndex: num,             //请求哪一页的数据
                    pageNum: parseInt(this.page_num),     //每页的条数
                    pageSize: 10,               //后端要求写10
                    userName: this.search_input
                },
                url: '/sysUser/isExist',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages,
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
        //获取角色
        get_info: function () {
            let _this = this;
            let obj = {
                type: "get",
                data: {},
                dataType: 'json',
                url: '/sysRole/querySysRole',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.info = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        // 点击变更角色按钮执行
        set: function (userChName, userId) {
            this.select = '';
            this.prompt1 = '';
            this.userChName = userChName;
            this.userid = userId;
            let _this = this;
            layer.open({
                title: '变更角色设置',
                type: 1,
                content: $('.popSet'),
                area:['300px','250px'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.select == '') {
                        layer.msg('请选择一个角色');
                        return;
                    }
                    let obj = {
                        type: 'post',
                        data: {
                            userId: _this.userid,
                            roleId: _this.select
                        },
                        dataType: 'json',
                        url: '/sysUser/updateUser',
                        success: function (data) {
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
        //点击修改密码按钮
        popPass: function (userid, username) {
            this.prompt4 = '';
            this.newpass = '';
            this.newpass_ = '';
            this.prompt = '';
            this.userid = userid;
            this.username = username;
            let _this = this;
            layer.open({
                title: '修改密码',
                type: 1,
                content: $('.popPass'),
                area:['500px','300px'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.newpass.trim() === '') {
                        layer.msg('新密码不能为空');
                        return;
                    }
                    if (_this.newpass.trim().length < 6 || _this.newpass.trim().length > 12) {
                        layer.msg('密码长度必须是6-12位');
                        return;
                    }
                    if (_this.newpass_ != _this.newpass) {
                        layer.msg('两次输入密码不一致');
                        return;
                    }
                    let data = {
                        userId: _this.userid,
                        userPassword: _this.newpass
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/sysUser/updateUser',
                        success: function (data) {
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
        //旧密码失焦事件
        checkoddpass: function () {
            ////console.log(this.oddpass);
            if (this.oddpass.trim() === '') {
                this.prompt4 = '旧密码不能为空';
                $('#prompt4').show();
                return;
            }
            let _this = this;
            let data = {
                userName: this.username,
                oldUserPassword: this.oddpass
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/sysUser/isCorrectUserPassword',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.flag1 = true;
                        $('#prompt4').css('color', 'green');
                        _this.prompt4 = data.msg;
                        $('#prompt4').show();
                    } else {
                        _this.flag1 = false;
                        $('#prompt4').css('color', 'darkred');
                        _this.prompt4 = data.msg;
                        $('#prompt4').show();
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击搜索框搜索
        search: function () {
            let _this = this;
            _this.search_input=_this.store_search;
            let obj = {
                type: 'get',
                data: {
                    pageIndex: 1,
                    pageNum: parseInt(this.page_num),
                    pageSize: 10,
                    userName: this.search_input.trim()
                },
                dataType: 'json',
                url: '/sysUser/isExist',
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg('加载中...', {time: 500});
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
                    }
                    ////console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击改变是否启用
        changeType: function (userId, state, index) {
            let _this = this;
            if (state == 1) {
                state = 0;
            } else {
                state = 1;
            }
            let obj = {
                type: 'post',
                data: {
                    userId: userId,
                    state: state
                },
                dataType: 'json',
                url: "/sysUser/isStartUsing",
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.datas[index].state = state;
                        layer.msg(data.msg);
                        // if ($('#' + userId).is('layui-form-onswitch')) {
                        //     $('#' + userId).removeClass('layui-form-onswitch');
                        //     window.location.reload();
                        // } else {
                        //     $('#' + userId).addClass('layui-form-onswitch');
                        //     window.location.reload();
                        // }
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
        //点击弹出添加管理员框
        popadd: function () {
            $('.layui-layer-btn0').css('background-color', '#009688');
            this.name = '';
            this.ChName = '';
            this.pass = '';
            this.select_ = '';
            let _this = this;
            layer.open({
                title: '添加管理员',
                type: 1,
                content: $('.popAdd'),
                area:['40%','70%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.ChName.trim() == ''||!(/[\u4e00-\u9fa5]/.test(_this.ChName.trim()))) {
                        layer.msg('中文名不能为空或格式错误');
                        return;
                    }
                    if(_this.ChName.length>10){
                        layer.msg('输入中文名称超长');
                        return;
                    }
                    if (_this.pass.trim() == '') {
                        layer.msg('密码不能为空');
                        return;
                    }
                    if (_this.pass.length < 6 || _this.pass.length > 12) {
                        layer.msg('密码长度必须是6-12位');
                        return;
                    }
                    if (_this.select_ == '') {
                        layer.msg('请选择一个角色');
                        return;
                    }
                    if (_this.flag == false) {
                        layer.msg('用户名已存在或格式错误');
                        return;
                    }
                    let data = {
                        userName: _this.name,
                        userChName: _this.ChName,
                        userPassword: _this.pass,
                        roleId: _this.select_
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/sysUser/addUser',
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
            })
        },
        //验证帐号是否可用
        checkName: function () {
            let _this = this;
            if (this.name.trim().length<6||this.name.trim().length>20||this.name.trim()===''||!(/^[a-zA-z]\w{5,21}$/.test(this.name.trim()))) {
                layer.msg('格式错误！英文、数字6-20位');
                return;
            }
            let a = /[\u4e00-\u9fa5]/;
            let b = this.name.match(a);
            if(b){
                layer.msg('格式错误，不能包含中文!');
                return;
            }
            let obj = {
                type: 'get',
                data: {userName: this.name.trim()},
                dataType: 'json',
                url: '/sysUser/queryUserByUserName',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.flag = true;
                        layer.msg(data.msg);
                    } else {
                        _this.flag = false;
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击解锁
        unlocked: function (username) {
            let _this = this;
            let data = {
                userName: username
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/sysUser/unlockUser',
                success: function (data) {
                    ////console.log(data);
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
        }
    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
        },
        //监听变更角色框中下拉框中的值
        select: function () {
            if (this.select != '') {
                $('#prompt1').hide();
            } else {
                $('#prompt1').show();
            }
        },
        //监听添加管理员框中下拉框中的值
        select_: function () {
            if (this.select_ != '') {
                $('#prompt2').hide();
            } else {
                $('#prompt2').show();
            }
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