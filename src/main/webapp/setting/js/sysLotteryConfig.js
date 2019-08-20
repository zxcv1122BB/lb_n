/**
 * Created by ASUS on 2017/9/19.
 */
let app = new Vue({
    el: '#app',
    data: {
        datas: [],   //列表数据

        total: '',   //信息总条数
        pageNum: 50,  //默认每页5条
        status: '1',   //默认状态  1是启用，0是禁用.
        //添加
        imgUrl: '',   //图片url
        contentUrl: '',//内容url
        configureName: '',//标题
        style: "nav_cyan",//样式
        sysConfig1: '',//副标题
        sort: '',//排序
        //修改
        id1: '',
        imgUrl1: '',   //图片url
        contentUrl1: '',//内容url
        configure1: '',   //副标题
        configureName1: '',//标题
        style1: '',//样式
        sysConfig11: '',//副标题
        sort2: '',
    },
    created() {
        this.$nextTick(function () {
            this.getlayer();
        })
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
                status: parseInt(this.status) + ''
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/sysLotteryConfig/querySysLotteryConfigList',
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
            this.imgUrl = '';
            this.contentUrl = '';
            this.configureName = '';
            this.sysConfig1 = '';
            this.sort = '';
            let _this = this;
            layer.open({
                title: '添加彩种显示信息',
                type: 1,
                content: $('.popAdd'),
                area: ['30%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.configureName.trim() === '' || _this.configureName.trim().length < 2 || _this.configureName.trim().length > 6) {
                        layer.msg('配置中文名不能为空或格式错误(2-6个字符)');
                        return;
                    }
                    if (_this.contentUrl.trim() === '') {
                        layer.msg('内容路径不能为空');
                        return;
                    }
                    if (_this.imgUrl.trim() === '') {
                        layer.msg('图片路径不能为空');
                        return;
                    }
                    if (_this.style.trim() === '') {
                        layer.msg('样式不能为空');
                        return;
                    }
                    if (_this.sysConfig1.trim() === '') {
                        layer.msg('配置属性不能为空');
                        return;
                    }
                    if (!_this.sort || parseInt(_this.sort) <= 0) {
                        layer.msg('排序标记必须大于0');
                        return;
                    }
                    let data = {
                        imgUrl: _this.imgUrl,   //配置名称
                        contentUrl: _this.contentUrl,   //配置名称
                        configureName: _this.configureName,//配置中文名
                        style: _this.style,//开关，控制显示的属性的样式，0不加样式、1加样式
                        sysConfig1: _this.sysConfig1,//需要显示的属性
                        sort: parseInt(_this.sort),
                    };
                    ////console.log(data);
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/sysLotteryConfig/add',
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
        //点击改变状态
        changeType: function (item, index) {
            let _this = this;
            if (item.status == 0) {
                item.status = 1;
            } else if (item.status == 1) {
                item.status = 0;
            }
            let obj = {
                type: 'post',
                data: {
                    id: item.id,
                    status: item.status
                },
                dataType: 'json',
                url: "/sysLotteryConfig/update",
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].status = item.status;
                        layer.msg(data.msg);
                    } else {
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
        popMsg: function (item,index) {
            this.id1 = item.id;
       	    this.imgUrl1=item.imgUrl;
       	    this.contentUrl1=item.contentUrl;
            this.configureName1 = item.configureName;
            this.style1 = item.style;
            this.sysConfig11 = item.sysConfig1;
            this.sort2 = item.sort;
            let _this = this;
            layer.open({
                title: '修改彩种显示信息',
                type: 1,
                content: $('.popPass'),
                area: ['50%','80%'],
                btn: ['确定', '取消'],
                yes: function (index2) {
                    if (_this.configureName1.trim() === '' || _this.configureName1.trim().length < 2 || _this.configureName1.trim().length > 6) {
                        layer.msg('配置中文名不能为空或格式错误(2-6个字符)');
                        return;
                    }
                    if (_this.style1.trim() === '') {
                        layer.msg('样式不能为空');
                        return;
                    }
                    if (_this.sysConfig11.trim() === '') {
                        layer.msg('配置属性不能为空');
                        return;
                    }
                    if (!_this.sort2 || parseInt(_this.sort2) <= 0) {
                        layer.msg('排序标记必须大于0');
                        return;
                    }
                    let data = {
                        id: parseInt(_this.id1),        //配置id
               		    imgUrl:_this.imgUrl1,           //图片url
               		    contentUrl:_this.contentUrl1,   //内容url
                        configureName: _this.configureName1,//配置中文名
                        style: _this.style1,//样式
                        sysConfig1: _this.sysConfig11,//需要显示的属性
                        sort: parseInt(_this.sort2),//需要显示的属性
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/sysLotteryConfig/update',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                _this.datas[index].configureName = _this.configureName1;
                                _this.datas[index].sysConfig1 = _this.sysConfig11;
                                _this.datas[index].style = _this.style1;
                                layer.msg(data.msg);
                                layer.close(index2);
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