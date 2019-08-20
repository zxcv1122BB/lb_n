/**
 * Created by ASUS on 2017/9/19.
 */
let app = new Vue({
    el: '#app',
    data: {
        datas: [],   //列表数据

        total: '',   //信息总条数
        pageNum: 50,  //默认每页5条
        status: 3,   //默认状态  0是启用，1是禁用.
        ipAddress: '',   //绑定ip地址输入框
        flag: false,     //校验新增是否有重复IP
        remark: '',      //绑定ip备注信息

        oddIp: '',       //旧ip
        newIp: '',       //新ip
        flag2: true,     //校验修改ip是否与列表有重名ip
        remark_: '',     //修改信息里面的备注信息
        status_: ''   //修改信息里面的状态
    },
    created() {
        this.getlayer();
    },
    methods: {
        //加载layer
        getlayer() {
            layui.use('layer', function () {
                let layer = layui.layer;
            });
        },
        //初始化数据
        getdatas: function (num) {
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                states: parseInt(this.status),
                ip: $("#searchIp").val().trim()
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/IPBlacklist/getIpBlacklist',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.total = data.body.total;
                        _this.datas = data.body.list;
                        if (data.body.list.length > 0) {
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
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            ////console.log(data);
            base.sendRequest(obj);
        },
        //点击添加执行
        popadd: function () {
            this.ipAddress = '';
            this.remark = '';
            let _this = this;
            layer.open({
                title: '添加IP黑名单',
                type: 1,
                content: $('.popAdd'),
                area: ['50%','60%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.ipAddress.trim() === '') {
                        layer.msg('ip地址不能为空');
                        return;
                    }
                    if (_this.flag == false) {
                        layer.msg('ip格式不正确或已存在');
                        return;
                    }
                    let data = {
                        ip: _this.ipAddress.trim(),
                        remark: _this.remark
                    };
                    ////console.log(data);
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/IPBlacklist/addIpBlacklist',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
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
        //离开事件，校验是否有重复IP
        check_ip() {
            let url = /(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)/;
            let a = this.ipAddress.trim().match(url);
            if (a == null) {
                layer.msg('ip地址格式不正确');
                this.flag = false;
                return;
            } else {
                layer.msg('格式正确');
                this.flag = true;
            }
            let data = {
                ip: this.ipAddress.trim()
            };
            let _this = this;
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/IPBlacklist/isExistIp',
                success(data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.flag = true;
                    } else {
                        _this.flag = false;
                        layer.msg(data.msg);
                    }
                },
                error(msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击改变状态
        changeType: function (id, status, index) {
            let _this = this;
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            let data = {
                id: id,
                states: status
            };
            //console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: "/IPBlacklist/updateIpBlacklist",
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        _this.datas[index].states = status;
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
        //校验新IP是否重名
        check_newIp() {
            let url = /(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)/;
            let a = this.newIp.trim().match(url);
            if (a == null) {
                layer.msg('ip地址格式不正确');
                this.flag2 = false;
                return;
            } else {
                layer.msg('格式正确');
                this.flag2 = true;
            }
            let data = {
                ip: this.newIp.trim()
            };
            let _this = this;
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/IPBlacklist/isExistIp',
                success(data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.flag2 = true;
                    } else {
                        _this.flag2 = false;
                        layer.msg(data.msg);
                    }
                },
                error(msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击修改信息执行
        popMsg: function (id, ip, remark, status) {
            this.newIp = '';
            this.id = id;
            this.oddIp = ip;
            this.remark_ = remark;
            this.status_ = status;
            let _this = this;
            layer.open({
                title: '修改信息',
                type: 1,
                content: $('.popPass'),
                area: ['25%','60%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.newIp.trim() === '') {
                        layer.msg('新ip不能为空');
                        return;
                    }
                    if (!_this.flag2) {
                        layer.msg('新ip已存在列表中或格式不正确!');
                        return;
                    }
                    let data = {
                        id: _this.id,
                        remark: _this.remark_,
                        states: _this.status_,
                        ip: _this.newIp.trim()
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/IPBlacklist/updateIpBlacklist',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
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
        //点击删除
        delBlacklistIp: function (id, index_) {
            let _this = this;
            layer.open({
                title: '提示信息',
                type: 1,
                content: $('.popDel'),
                area: ['20%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    let data = {
                        id: id
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/IPBlacklist/deleteBlacklist',
                        success: function (data) {
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                _this.datas.splice(index_, 1);
                            } else {
                                layer.close(index);
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest(obj);
                },
            });
        }
    },
    watch: {
        //监听页码每页条数
        pageNum: function () {
            this.getdatas(1);
        },
        //监听状态下拉框
        status: function () {
            ////console.log(this.status);
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