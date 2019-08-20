/**
 * Created by ASUS on 2017/9/25.
 */
let app = new Vue({
    el: "#app",
    data: {
        datas: [], //站内信列表
        page_num: 50, //分页中每页显示多少条数据
        total: '',   //总共有多少条记录

        changeTitle: "", //查看弹出层的标题
        changeContent: "", // 查看弹出层的内容
        addresser: "", //查看弹出层的发件人
        receive: "", //查看弹出层的接收人

        addText: "", //添加弹出框标题绑定
        addContents: "", //添加弹出框内容绑定
        sendType: '',    //绑定发送方式
        userType:2,
        
        search_input: '',//绑定搜索框
        search_inputUser: "", //绑定用户搜索框
        userName: [], //单选的名称

        flag: false,     //全选默认为假
        zNodes: [], //单独选择的列表
        member: [], //全选用户的数组id
        //member_userName: [],//记录要显示的帐号

        flag_zu: false,    //选择组全选默认为假
        zGroups: [],    //组选择的列表
        zu: [],          //记录选择的哪个组
        //zu_name: [],     //记录选择组的名称

        send_: [],        //记录发送传的id数据
        search_content:'',   //存储用户当前输入，但不直接带入到分页时的搜索条件中
    },
    created(){
        this.getlayer();
    },
    methods: {
        //加载layer
        getlayer(){
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
        //获取站内信列表 带模糊搜索
        getdatas: function (num) {
            let _this = this;
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5,
                title: this.search_input.trim()
            };
            let options = {
                type: 'get',
                url: "/sysMessage/querySysMessageTextByTitle",
                data: data,
                dataType: 'json',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        if(data.body.list.length>0){
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,
                                currentPage: 1
                            });
                        }else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage:1
                            });
                        }
                        /*if (data.body.pages === 0) {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1
                            });
                        } else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages
                            });
                        }*/
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //获取单发用户列表
        getdatasA: function (num_) {
            let _this = this;
            let data = {
                pageIndex: num_,
                pageNum: 5,
                pageSize: 5
            };
            ////console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                // url: '/user/selectUsersByVip',
                url: '/user/queryUserList',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.zNodes = data.body.list;
                        if (data.body.pages === 0) {
                            $('#fenyeA').jqPaginator('option', {
                                totalPages: 1
                            });
                        } else {
                            $('#fenyeA').jqPaginator('option', {
                                totalPages: data.body.pages
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
        //点击弹出添加框
        send: function () {
            this.addText = '';
            this.addContents = '';
            this.member = [];
            this.zu = [];
            this.sendType = '';
            this.send_ = [];
            this.flag = false;
            this.flag_zu = false;
            let _this = this;
            layer.open({
                title: '发送信息',
                type: 1,
                content: $('.popAdd'),
                area:['40%','85%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.addText.trim() === "") {
                        layer.msg('标题不能为空');
                        return;
                    }
                    if (_this.addContents.trim() === "") {
                        layer.msg('内容不能为空');
                        return;
                    }
                    if (_this.sendType === '') {
                        layer.msg('请选择发送的类型');
                        return;
                    }
                    if (_this.sendType === '1') {
                        if (_this.member.length === 0) {
                            layer.msg('请选择至少一名会员');
                            return;
                        }
                    }
                    if (_this.sendType == '2') {
                        if (_this.zu.length === 0) {
                            layer.msg('请选择至少一个组');
                            return;
                        }
                    }
                    if (_this.sendType === '1') {
                        _this.send_ = _this.member;
                    } else if (_this.sendType == '2') {
                        _this.send_ = _this.zu;
                    }
                    let data = {
                        title: _this.addText.trim(),
                        contents: _this.addContents.trim(),
                        sendType: _this.sendType,
                        userType:_this.userType,
                        // receiver: JSON.stringify(_this.send_)
                        receiver: _this.send_.join(',')
                    };
                    ////console.log(data);
                    let options = {
                        type: "post",
                        url: "/sysMessage/addSysMessageText",
                        dataType: "json",
                        data: data,
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.msg(data.msg);
                                layer.close(index);
                                setTimeout(function () {
                                    window.location.reload()
                                }, 500);
                            } else {
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg)
                        }
                    };
                    base.sendRequest(options);
                },
                btn2: function () {
                }
            })
        },
        //点击查看按钮
        messageLookOver: function (id) {
            let _this = this;
            let data = {
                id: id
            };
            ////console.log(data);
            let options = {
                type: "get",
                url: "/sysMessage/querySysMessageTextById",
                data: data,
                dataType: "json",
                success: function (data) {
                    ////console.log(data);
                    _this.changeTitle = data.body.title;
                    _this.changeContent = data.body.contents;
                    _this.addresser = data.body.createUser;
                    _this.receive = data.body.receiverName;
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
            layer.open({
                title: '查看信息',
                type: 1,
                content: $('.popLook'),
                area:['50%','80%'],
                btn: ['关闭'],
                yes: function (index) {
                    layer.close(index);
                }
            })
        },
        //添加层选择用户发送(单独选择)
        sendUser: function () {
            this.member = [];
            let _this = this;
            layer.open({
                title: '成员列表',
                type: 1,
                content: $('.popPermission'),
                area:['40%','95%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    layer.close(index);
                },
                btn2: function () {
                }
            })
        },
        //点击check保存帐号--暂时不用
        keep_name: function (userName) {
            if (this.member_userName.length > 0) {
                for (let i = 0; i < this.member_userName.length; i++) {
                    if (this.member_userName[i] == userName) {
                        this.member_userName.splice(i, 1);
                        return;
                    }
                }
                this.member_userName.push(userName);
            } else {
                this.member_userName.push(userName);
            }

        },
        //添加层选择组发送
        sendGroup: function () {
            let _this = this;
            let options = {
                type: "get",
                url: "/user/queryAllUsername",
                data: {},
                dataType: "json",
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.zGroups = data.body;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
            layer.open({
                title: '组发',
                type: 1,
                content: $('.popZu'),
                area:['40%','50%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    layer.close(index);
                },
                btn2: function () {
                }
            })
        },
        //单发选择弹出层的全选
        seleceAll: function () {
            if (this.flag) {
                this.member = [];
                //this.member_userName = [];
                for (let i = 0; i < this.zNodes.length; i++) {
                    this.member.push(this.zNodes[i].uid);
                    //this.member_userName.push(this.zNodes[i].userName);
                }
            } else {
                this.member = [];
                //this.member_userName = [];
            }
        },
        //点击全选组发送全选
        selectAll_zu: function () {
            if (this.flag_zu) {
                this.zu = [];
                for (let i = 0; i < this.zGroups.length; i++) {
                    this.zu.push(this.zGroups[i].vipId);
                    //this.zu_name.push(this.zGroups[i].name);
                }
            } else {
                this.zu = [];
                //this.zu_name = [];
            }
        },
        //页面点击搜索
        search: function () {
            //if (this.search_input.trim()=='')return;
            let _this = this;
            this.search_input = this.search_content;
            let data = {
                pageIndex: 1,
                pageNum: 5,
                pageSize: 5,
                title: this.search_input.trim()
            };
            let options = {
                type: 'get',
                url: '/sysMessage/querySysMessageTextByTitle',
                dataType: 'json',
                data: data,
                success: function (data) {
                    ////console.log(data);
                    layer.msg('加载中...',{time:500});
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        ////console.log(_this.datas);
                        if(data.body.list.length>0){
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages,
                                currentPage: 1
                            });
                        }else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage:1
                            });
                        }
                       /* 
                        $('#fenye').jqPaginator('option', {
                            totalPages: data.body.pages,
                            currentPage: 1
                        });*/
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //用户选择点击搜索
        searchUser: function () {
            let _this = this;
            let data = {
                pageIndex: 1,
                pageNum: 5,
                pageSize: 10,
                username: this.search_inputUser,
                // VIP_ID: this.uid
            };
            let options = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/user/queryUserList',
                success: function (data) {
                    ////console.log(data);
                    layer.msg('加载中...',{time:500});
                    if (data.code == 200) {
                        _this.zNodes = data.body.list;
                        _this.total = data.body.total;
                        $('#fenyeA').jqPaginator('option', {
                            totalPages: data.body.pages,
                        });
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //点击删除弹框
        deleteList: function (id, index_) {
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
                    let options = {
                        type: 'post',
                        url: '/sysMessage/deleteSysMessageTextById',
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1000)
                            } else {
                                layer.close(index_);
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg)
                        }
                    };
                    base.sendRequest(options);
                },
                btn2: function () {
                }
            })
        },
    },
    watch: {
        //单发绑定的checkbox
        member: function () {
            if (this.member.length == this.zNodes.length) {
                this.flag = true;
            } else {
                this.flag = false;
            }
        },
        //组发记录的checkbox
        zu: function () {
            if (this.zu.length == this.zGroups.length) {
                this.flag_zu = true;
            } else {
                this.flag_zu = false;
            }
        },
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
        }
    }
});
// 加载分页功能 主页
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

// 加载分页功能 单发用户页
$.jqPaginator('#fenyeA', {
    totalPages: 1,      //多少页数据
    visiblePages: 10,   //最多显示几页
    currentPage: 1,     //当前页
    wrapper: '<ul class="pagination"></ul>',
    first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
    prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
    next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
    last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
    page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
    onPageChange: function (num_, type) {
        app.getdatasA(num_);
    }
});