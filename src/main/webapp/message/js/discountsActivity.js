/**
 * Created by ASUS on 2017/9/14.
 */
// var dropBown=[{Select:"1"},{Select:"2"},{Select:"3"},{Select:"4"}];
let app = new Vue({
    el: "#app",
    data: {
        datas: [],//公告列表
        page_num: 50,//分页中每页显示多少条数据
        total: 0,   //共多少条数据

        title: '', //添加弹出层标题绑定
        title_pic: '',//添加弹出层图片链接绑定
        flag:false,   //检测图片url格式是否正确
        content_pic: '', //添加弹出层内容链接绑定
        flag2:false,    //检测内容url格式是否正确

        changeTitle: '', //更改框标题绑定
        changeTitle_pic: '',//小图片
        flag3:true,
        changeContent: '', //大图片
        flag4:true,
        changeId: "", //更改框中编号绑定
    },
    created: function () {
        this.$nextTick(function () {
            this.get_time();
        })
    },
    methods: {
        //获取公告信息列表
        getdatas: function (num) {
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5
            };
            let _this = this;
            let options = {
                type: "get",
                url: "/sysActivity/querySysActivityList",
                data: data,
                dataType: 'json',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                    	if(data.body.list){
                    		_this.datas = data.body.list;
	                        _this.total = data.body.total;
	                        //分页的(右边点击)
	                        if(data.body.pageSize===0){
								$('#fenye').jqPaginator('option', {
									totalPages: 1,
									currentPage: 1
								});
							}else{
								$('#fenye').jqPaginator('option', {
									totalPages: data.body.pages ? data.body.pages:1,
									currentPage: num
								});
							}
                    	}
                        
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
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
            laydate.render({
                elem: '#startDate_edit', //指定元素
                format: 'yyyy-MM-dd',
                //type: 'datetime',
                //value: ""
            });
            laydate.render({
                elem: '#endDate_edit', //指定元素
                format: 'yyyy-MM-dd',
                //type: 'datetime',
                //value: ""
            })
        },
        //点击弹出添加公告框
        popup: function () {
            this.title = '';
            this.title_pic = '';
            this.content_pic = '';
            $('#startDate').val('');
            $('#endDate').val('');
            let _this = this;
            layer.open({
                title: '添加优惠活动',
                type: 1,
                content: $('.popAdd'),
                area:['70%','85%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.title.trim() === ""||_this.title.trim().length>20) {
                        layer.msg('标题不能为空或长度超长最多20字');
                        return;
                    }
                    if (_this.title_pic.trim() === ''||_this.title_pic.trim().length>254) {
                        layer.msg('标题链接不能为空或长度超长不超过255');
                        return;
                    }
                    if (_this.content_pic.trim() === ""||_this.content_pic.trim().length>254) {
                        layer.msg('内容不能为空或长度超长不超过255');
                        return;
                    }
                    let start = $('#startDate').val();
                    let end = $('#endDate').val();
                    if (start == '' || end == '') {
                        layer.msg('请选择有效期');
                        return;
                    }
                    if (_this.title_pic.trim().length>255) {
                        layer.msg('标题链接长度不能超过255');
                        return;
                    }
                    if (_this.content_pic.trim().length>255) {
                        layer.msg('内容长度不能超过255');
                        return;
                    }
//                  if(_this.flag == false){
//                      layer.msg('图片url格式不正确');
//                      return;
//                  }
//                  if(_this.flag2 == false){
//                      layer.msg('内容url格式不正确');
//                      return;
//                  }
                    let data = {
                        title: _this.title.trim(),
                        titleUrl: _this.title_pic.trim(),
                        contentUrl: _this.content_pic.trim(),
                        startTime: start,
                        endTime: end
                    };
                    ////console.log(data);
                    let options = {
                        type: 'post',
                        url: "/sysActivity/addSysActivity",
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
        //点击删除弹出层
        deleteList: function (id, index_) {
            let _this = this;
            layer.open({
                title: '提示',
                type: 1,
                content: $('.popDel'),
                area: ['20%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    let data = {
                        id: id
                    };
                    let options = {
                        type: "post",
                        url: '/sysActivity/deleteSysActivityById',
                        dataType: "json",
                        data: data,
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                },500)
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
        //点击更改弹出层的内容
        changeButton: function (id, title, title_pic, content_pic, start, end) {
            this.changeId = id;
            this.changeTitle = title;
            this.changeTitle_pic = title_pic;
            this.changeContent = content_pic;
            $('#startDate_edit').val(start);
            $('#endDate_edit').val(end);
            let _this = this;
            layer.open({
                title: '更改活动信息',
                type: 1,
                content: $('.popEdit'),
                area:['44%','37%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.changeTitle.trim() === ''||_this.changeTitle.trim().length>20) {
                        layer.msg('标题不能为空或长度超长最多20字');
                        return;
                    }
                    if (_this.changeTitle_pic.trim() === ''||_this.changeTitle_pic.trim().length>254) {
                        layer.msg('标题链接不能为空或长度超长不超过255');
                        return;
                    }
                    if (_this.changeContent.trim() === ''||_this.changeContent.trim().length>254) {
                        layer.msg('内容不能为空或长度超长不超过255');
                        return;
                    }
                    let start = $('#startDate_edit').val();
                    let end = $('#endDate_edit').val();
                    if (start == '' || end == '') {
                        layer.msg('请选择有效期');
                        return;
                    }
                    if(_this.flag3 == false){
                        layer.msg('图片url格式不正确');
                        return;
                    }
                    if(_this.flag4 == false){
                        layer.msg('内容url格式不正确');
                        return;
                    }
                    let data = {
                        id: _this.changeId,
                        title: _this.changeTitle.trim(),
                        titleUrl: _this.changeTitle_pic.trim(),
                        contentUrl: _this.changeContent.trim(),
                        startTime: start,
                        endTime: end
                    };
                    let options = {
                        type: "post",
                        url: '/sysActivity/updateSysActivity',
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
        //检测图片url是否是正确url地址
//      checkUrl(){
//          let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
//          let a = this.title_pic.match(url);
//          if (a == null) {
//              layer.msg('图片url格式不正确');
//              this.flag = false;
//          }else {
//              layer.msg('图片url格式正确');
//              this.flag = true;
//          }
//      },
        //检测内容url是否是正确url地址
//      checkUrl_2(){
//          let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
//          let a = this.content_pic.match(url);
//          if (a == null) {
//              layer.msg('内容url格式不正确');
//              this.flag2 = false;
//          }else {
//              layer.msg('内容url格式正确');
//              this.flag2 = true;
//          }
//      },
        //修改框url是否正确
        checkUrl_3(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.changeTitle_pic.match(url);
            if (a == null) {
                layer.msg('图片url格式不正确');
                this.flag3 = false;
            }else {
                layer.msg('图片url格式正确');
                this.flag3 = true;
            }
        },
        //修改框url是否正确
        checkUrl_4(){
            let url = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
            let a = this.changeContent.match(url);
            if (a == null) {
                layer.msg('图片url格式不正确');
                this.flag4 = false;
            }else {
                layer.msg('图片url格式正确');
                this.flag4 = true;
            }
        }
    },
    watch: {
        //监听页码下拉框的值
        page_num: function () {
            this.getdatas(1);
        },
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
