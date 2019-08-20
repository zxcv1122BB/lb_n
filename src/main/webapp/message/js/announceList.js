/**
 * Created by ASUS on 2017/9/14.
 */
let app = new Vue({
    el: "#app",
    data: {
        isText: false, //隐藏搜索文字提示

        datas: [],//公告列表
        page_num: 50, //分页中每页显示多少条数据
        total: '',   //分页显示总页数

        title: "", //添加弹出框标题，必填
        content: "", //添加弹出框内容，必填
        select: "", //添加弹出框下拉框绑定
        id: '',//公告id
        is_important:0, //是否为重要公告，1为重要，0为不重要。默认为0

        search_input: '',//绑定搜索框
        hint: '', // 添加 || 删除 提示信息的文字

        changeTitle: "", //更改弹出框标题
        changeContent: "", //更改弹出框内容
        changeId: "", //更新弹窗框id。必须
        changeContentCss:'',//更改是否为重要信息
        search_content:'', //
    },
    created(){
        this.getlayer();
    },
    methods: {
        initCkeditor(){
            
        },
        //加载layer
        getlayer(){
            layui.use("layer", function () {
                let layer = layui.layer;
            });
        },
        //获取公告信息列表
        getdatas: function (num,flag) {
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 5,
                title: this.search_input.trim()
            };
            if(flag){
                data.title=this.search_content;
                this.search_input = this.search_content
            }
            ////console.log(data);
            let _this = this;
            let options = {
                type: "get",
                url: "/sysBulletin/querySysBulletinByTitle",
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
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);

        },
        //点击弹出添加框
        popupAnnouncement: function () {
            this.title = '';
            this.content = '';
            this.select = '';
            let _this = this;
            editor.setContent('');
            layer.open({
                title: '公告添加',
                type: 1,
                content: $('.popAdd'),
                area:['60%','85%'],
                btn: ['确定', '关闭'],
                yes: function () {
                    if (_this.title.trim() === ""||_this.title.trim().length>20) {
                        layer.msg('标题不能为空或长度过长(最多20个字符)');
                        return;
                    }
//                  var content = editor.getData();
					var content = editor.getContent();
                    if (content.trim() === "") {
                        layer.msg('内容不能为空');
                        return;
                    }
                    let data = {
                        title: _this.title, //公告标题
                        // content: _this.content, //公告内容
                        content: content,//公告内容
                        contentCss:_this.is_important  //是否为重要信息
                    };
                    ////console.log(data);
                    let options = {
                        type: 'post',
                        url: '/sysBulletin/addSysBulletin',
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            ////console.log(data);
                            if(data.code==200){
                                layer.close('page');
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
        //点击删除弹出层
        deleteList: function (listId, listIndex) {
            let _this = this;
            layer.open({
                title:'提示信息',
                type:1,
                content:$('.popDel'),
                area:['20%'],
                btn:['确定','取消'],
                yes:function (index) {
                    let data = {
                        id: listId,
                        index: listIndex
                    };
                    let options = {
                        type: 'post',
                        url: '/sysBulletin/deleteSysBulletinById',
                        data: data,
                        dataType: 'json',
                        success: function (data) {
                            ////console.log(data);
                            if(data.code==200){
                                layer.close(index);
                                layer.msg(data.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                },500)
                            }else {
                                layer.close('page');
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
        //点击弹出更改框
        changeList: function (id, title, content,contentCss) {
            let _this = this;
            this.changeTitle = title;
            this.changeContent = content;
            this.changeContentCss = contentCss;
            this.changeId = id;
//          editor1.setData(content);
			editor1.setContent(content);

            layer.open({
                title:'信息更改',
                type:1,
                content:$('.popEdit'),
                area:['60%','85%'],
                btn:['确定','关闭'],
                yes:function () {
                    if (_this.changeTitle.trim() === ""||_this.changeTitle.trim().length>20) {
                        layer.msg('标题不能为空或长度过长(最多20个字符)');
                        return;
                    }
//                  var changeContent=editor1.getData();
                    var changeContent = editor1.getContent();
                    if (changeContent.trim() === "") {
                        layer.msg('内容不能为空');
                        return;
                    }
                    let data = {
                        id: _this.changeId,
                        title: _this.changeTitle, //公告标题
                        content: changeContent, //公告内容
                        contentCss:_this.changeContentCss   //重要信息1为重要，0为不重要
                    };
                    ////console.log(data);
                    let options = {
                        type: "post",
                        url: "/sysBulletin/updateSysBulletin",
                        data: data,
                        dataType: "json",
                        success: function (data) {
                            if (data.code==200){
                                layer.close('page');
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
                btn2:function () {}
            })
        },
        //更改公告框点击是否重要公告按钮事件
        edit_important:function (num) {
            if(num == '1'){
                this.changeContentCss = '0';
            }else{
                this.changeContentCss = '1';
            }
        },
        //点击搜索框搜索
        search: function () {
            this.getdatas(1,true);
        },
        //添加公告框点击是否重要公告按钮事件
        change_important:function (num) {
            if(num==1){
                this.is_important = 0;
            }else{
                this.is_important = 1;
            }
        },
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
