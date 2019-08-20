/**
 * Created by ASUS on 2017/9/25.
 */
let app = new Vue({
    el: "#app",
    data: {
        datas: [], //轮播图片列表
        page_num: 50, //分页中每页显示多少条数据
        total:'',    //总条数

        //添加
        href_title:'',//跳转到新页面的标题
        title: '', //图片url链接，必填
        //flag:false,//正则验证url
        title_:'',//点击图片跳转url
        //flag_:false,//正则验证跳转url
        select_a: "", //添加弹出框下拉框绑定

        //更改
        href_title_2:'',//跳转到新页面的标题
        titleUrl: "", //框的url绑定
        //flag2:true,   //正则验证url
        titleUrl_:'', //点击图片跳转url
        //flag2_:true,  //正则验证跳转url
        select_b: "", //更改弹出框下拉框绑定

        dropBown: [      //添加弹出框优先级选择
            {Select: 1},
            {Select: 2},
            {Select: 3},
            {Select: 4},
            {Select: 5}
        ],
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
        //获取轮播图片信息列表
        getdatas: function (num) {
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5
            };
            let _this = this;
            let options = {
                type: "get",
                url: "/sysAdvPicture/querySysAdvPictureList",
                data: data,
                dataType: 'json',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.datas = data.body.list;
                        _this.total = data.body.total;
                        if(data.body.list.length>0){
                            $('#fenye').jqPaginator('option', {
                                totalPages: data.body.pages
                            });
                        }else {
                            $('#fenye').jqPaginator('option', {
                                totalPages: 1,
                                currentPage:1
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
        //点击弹出添加图片框
        popup: function () {
            this.href_title = '';
            this.title = '';
            this.title_ = '';
            this.select_a = '';
            let _this = this;
            layer.open({
                title: '添加轮播图片',
                type: 1,
                content: $('.popAdd'),
                area:['50%','75%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if(_this.href_title.trim()===''||_this.href_title.trim().length>5){
                        layer.msg('标题不能为空或长度超长');
                        return;
                    }
                    if (_this.title.trim().length>254) {
                        layer.msg('链接地址长度超长不超过255');
                        return;
                    }
                    if(_this.title_.trim().length>254){
                        layer.msg('跳转地址长度超长不超过255');
                        return;
                    }
                    if (_this.select_a == '') {
                        layer.msg('请选择优先级');
                        return;
                    }
                    // if(_this.flag == false||_this.flag_ == false){
                    //     layer.msg('图片url或跳转url格式不正确!');
                    //     return;
                    // }
                    let data = {
                        title:_this.href_title,
                        pictureUrl: _this.title.trim(),
                        destinationUrl:_this.title_.trim(),
                        priority: _this.select_a
                    };
                    let options = {
                        type: 'post',
                        url: "/sysAdvPicture/addSysAdvPicture",
                        dataType: "json",
                        data: data,
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
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
        //点击更改按钮
        changeButton: function (title,pictureUrl,destinationUrl, priority, id) {
            this.href_title_2 = title;
            this.titleUrl = pictureUrl;
            this.titleUrl_ = destinationUrl;
            this.select_b = priority;
            let _this = this;
            layer.open({
                title: '更改图片信息',
                type: 1,
                content: $('.popEdit'),
                area:['60%','70%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.href_title_2.trim() === ""||_this.href_title_2.trim().length>5) {
                        layer.msg('标题不能为空或长度超长');
                        return;
                    }
                    if (_this.titleUrl.trim().length>254) {
                        layer.msg('链接地址长度超长');
                        return;
                    }
                    if(_this.titleUrl_.trim().length>254){
                        layer.msg('跳转地址长度超长');
                        return;
                    }
                    if (_this.select_b == "") {
                        layer.msg('请选择优先级');
                        return;
                    }
                    // if(_this.flag2 == false||_this.flag2_ == false){
                    //     layer.msg('图片url或跳转url格式不正确');
                    //     return;
                    // }
                    let data = {
                        title:_this.href_title_2,
                        pictureUrl: _this.titleUrl.trim(),
                        destinationUrl:_this.titleUrl_.trim(),
                        priority: _this.select_b,
                        id: id
                    };
                    let options = {
                        type: 'post',
                        url: "/sysAdvPicture/updateSysAdvPicture",
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            if (data.code==200){
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
                            //console.log(msg)
                        }
                    };
                    base.sendRequest(options);
                },
                btn2: function () {}
            })
        },
        //点击删除弹出层
        deleteList: function (id, index_) {
            let _this = this;
            layer.open({
                title:'提示信息',
                type:1,
                content:$('.popDel'),
                area:['20%'],
                btn:['确定','取消'],
                yes:function (index) {
                    let data = {
                        id: id
                    };
                    let options = {
                        type: "post",
                        url: '/sysAdvPicture/deleteSysAdvPictureById',
                        dataType: "json",
                        data: data,
                        success: function (data) {
                            if (data.code==200){
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                },500)
                            }else {
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
                btn2:function () {}
            })
        },
        //添加框检测图片url是否正确
        checkUrl(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.title.match(url);
            if (a == null) {
                layer.msg('图片url格式不正确');
                this.flag = false;
            }else {
                layer.msg('图片url格式正确');
                this.flag = true;
            }
        },
        //添加框检测跳转url是否正确
        checkUrl_(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.title_.match(url);
            if (a == null) {
                layer.msg('url格式不正确');
                this.flag_ = false;
            }else {
                layer.msg('url格式正确');
                this.flag_ = true;
            }
        },
        //修改框检测url是否正确
        checkUrl_2(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.titleUrl.match(url);
            if (a == null) {
                layer.msg('图片url格式不正确');
                this.flag2 = false;
            }else {
                layer.msg('图片url格式正确');
                this.flag2 = true;
            }
        },
        //修改框检测跳转url是否正确
        checkUrl_2_(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.titleUrl_.match(url);
            if (a == null) {
                layer.msg('url格式不正确');
                this.flag2_ = false;
            }else {
                layer.msg('url格式正确');
                this.flag2_ = true;
            }
        }
    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
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