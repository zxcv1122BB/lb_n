let app = new Vue({
    el: '#app',
    data: {
        datas: [],       //数据
        page_num: 50, //分页中每页显示多少条数据
        total: '',   //分页显示总页数
        search_input:'',    //搜索框
        state:1,            //状态，默认1为启用，0为禁用

        exportUser: base.BASE_URL,

        money:'',           //面额
        count:'',           //数量
        status:1,           //状态，默认1为启用，0为停用
        zGroups: [],        //组选择的列表
        select_group: [],    //显示已选择的组(用来显示)
        zu: [],             //记录选择的哪个组
    },
    created() {
        this.$nextTick(function () {
            this.getlayer();
            this.get_time();
        })
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
                //type: 'datetime',
                //value: ""
            });
            laydate.render({
                elem: '#endDate', //指定元素
                format: 'yyyy-MM-dd',
                //type: 'datetime',
                //value: ""
            });
        },
        //加载数据
        getdatas(num) {
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5,
                batch:this.search_input.trim(),
                flag:this.state
            };
            let _this = this;
            let options = {
                type: "get",
                url: "/preferentialCard/queryPreferentialCardList",
                data: data,
                dataType: 'json',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
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
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
        },
        //点击搜索查询
        search(){
            this.getdatas(1);
        },
        //删除卡卷
        delCoupon(id) {
            let _this = this;
            layer.open({
                title: '提示信息',
                type: 1,
                content: $('.popDel'),
                area: ['20%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    let data = {
                        id: id,
                    };
                    let options = {
                        type: 'post',
                        url: '/preferentialCard/deletePreferentialCard',
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500)
                            } else {
                                layer.close(index);
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
        //点击改变状态
        changeType(id, status, index) {
            let _this = this;
            if (status == 1) {
                status = 0;
            } else {
                status = 1;
            }
            let obj = {
                type: 'post',
                data: {
                    id: id,
                    status: status
                },
                dataType: "json",
                url: '/preferentialCard/updatePreferentialCard',
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].status = status;
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
        //点击选择Vip组
        sendGroup() {
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
                title: '选择vip组',
                type: 1,
                content: $('.popZu'),
                area: ['40%', '70%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    _this.select_group = [];
                    if (_this.zu.length != 0) {
                        for (let i = 0; i < _this.zu.length; i++) {
                            for (let j = 0; j < _this.zGroups.length; j++) {
                                if (_this.zu[i] == _this.zGroups[j].vipId) {
                                    _this.select_group.push(_this.zGroups[j].vipName);
                                }
                            }
                        }
                    } else {
                        _this.select_group = '';
                    }
                    layer.close(index);
                },
                btn2: function () {
                }
            })
        },
        //点击弹出添加框
        popAddCoupon: function () {
            this.money = '';
            this.count = '';
            this.zu = [];
            this.status = 1;
            $('#startDate').html('');
            $('#endDate').html('');
            let _this = this;
            layer.open({
                title: '卡卷添加',
                type: 1,
                content: $('.popAdd'),
                area: ['40%', '75%'],
                btn: ['确定', '关闭'],
                yes: function (index) {
                    if (_this.money.trim() === ""||_this.money.trim().length>10) {
                        layer.msg('面额不能为空或长度过长');
                        return;
                    }
                    if (_this.count.trim() === ""||_this.count.trim().length>10) {
                        layer.msg('数量不能为空或长度过长');
                        return;
                    }
                    if(_this.zu.length<1){
                        layer.msg('请至少选择一个Vip组');
                        return;
                    }
                    let start = $('#startDate').val();
                    let end = $('#endDate').val();
                    if(start == ''||end == ''){
                        layer.msg('请选择有效期');
                        return;
                    }
                    let vipArray = _this.zu.toString();
                    let data = {
                        count:_this.count.trim(),
                        vips:vipArray,
                        money:_this.money.trim(),
                        startTime:start,
                        endTime:end
                    };
                    ////console.log(data);
                    let options = {
                        type: 'post',
                        url: '/preferentialCard/addPreferentialCard',
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            ////console.log(data);
                            if(data.code==200){
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload()
                                }, 500);
                            }else {
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest(options);
                },
                btn2: function () {}
            })
        },
        //点击导出
        export1(){
            $('#form_').submit();
        },
    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
        },
        state:function () {
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