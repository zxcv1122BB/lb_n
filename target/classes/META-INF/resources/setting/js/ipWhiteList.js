/**
 * Created by ASUS on 2017/9/19.
 */
var app = new Vue({
    el: '#app',
    data: {
        datas: [],   //列表数据

        total: '',   //信息总条数
        pageNum: 50,  //默认每页5条
        status: 0,   //默认状态  0是启用，1是禁用.
        ipAddress: '',   //绑定ip地址输入框
        remark: '',      //绑定ip备注信息

        oddIp: '',       //旧ip
        newIp: '',       //新ip
        remark_: '',     //修改信息里面的备注信息
        status_: '',     //修改信息里面的状态
    },
    methods: {
        //初始化数据
        getdatas: function (num) {
            var _this = this;
            var data = {
                pageIndex: num,
                pageNum: parseInt(this.pageNum),
                pageSize: 10,
                status: parseInt(this.status)
            };
            var obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/ip/selectAllIps',
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
            base.sendRequest(obj);
        },
        //点击添加执行
        popadd: function () {
            this.ipAddress = '';
            this.remark = '';
            var _this = this;
            layer.open({
                title:'添加ip白名单',
                type:1,
                content:$('.popAdd'),
                area:['30%','60%'],
                btn:['确定','取消'],
                yes:function (index) {
                    if (_this.ipAddress.trim() === '') {
                        layer.msg('ip地址不能为空');
                        return;
                    }
                    var data = {
                        ipaddress: _this.ipAddress,
                        remark: _this.remark
                    };
                    ////console.log(data);
                    var obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/ip/addIp',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 100) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                },500);
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
                btn2:function () {}
            })
        },
        //点击改变状态
        changeType: function (id, status,index) {
            var _this = this;
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            var obj = {
                type: 'post',
                data: {
                    id: id,
                    status: status
                },
                dataType: 'json',
                url: "/ip/deleteIpOrOpen",
                success: function (data) {
                    if (data.code == 100) {
                       _this.datas[index].status = status;
                       layer.msg(data.msg);
                    }else {
                        layer.msg(data.msg);
                    }
                    ////console.log(data);
                },
                error: function (msg) {
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
            var _this = this;
            layer.open({
                title:'修改信息',
                type:1,
                content:$('.popPass'),
                area:['25%','55%'],
                btn:['确定','取消'],
                yes:function (index) {
                    if (_this.newIp.trim() === '') {
                        layer.msg('新ip不能为空');
                        return;
                    }
                    var data = {
                        id: _this.id,
                        ipaddress: _this.newIp,
                        remark: _this.remark_,
                        status: _this.status_
                    };
                    var obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/ip/updateIp',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 100) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                },500);
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
                btn2:function () {}
            })
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