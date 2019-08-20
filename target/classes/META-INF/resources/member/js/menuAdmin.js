/**
 * Created by ASUS on 2017/9/20.
 */
let app = new Vue({
    el: '#app',
    data: {
        datas: [],   //列表数据
        page_num: 50, //分页中每页显示多少条数据
        total: '',   //信息总条数
        search_input: '',    //绑定搜索框数据
        prompt3: '',         //搜索为空时的提示


        setting: {     //树插件设置
            check: {
                enable: true,
                chkStyle: "radio",
                radioType: "all"
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
        },
        zNodes: [], //树的数据
        /*{ id:1, pId:0, name:"父节点1 - 展开", open:true},
         { id:11, pId:1, name:"父节点11 - 折叠"},
         { id:111, pId:11, name:"叶子节点111"},
         { id:112, pId:11, name:"叶子节点112"},
         { id:113, pId:11, name:"叶子节点113"},
         { id:114, pId:11, name:"叶子节点114"},
         { id:12, pId:1, name:"父节点12 - 折叠"},
         { id:121, pId:12, name:"叶子节点121"},
         { id:122, pId:12, name:"叶子节点122"},
         { id:123, pId:12, name:"叶子节点123"},
         { id:124, pId:12, name:"叶子节点124"},
         { id:13, pId:1, name:"父节点13 - 没有子节点", isParent:true},
         { id:2, pId:0, name:"父节点2 - 折叠"},
         { id:21, pId:2, name:"父节点21 - 展开", open:true},
         { id:211, pId:21, name:"叶子节点211"},
         { id:212, pId:21, name:"叶子节点212"},
         { id:213, pId:21, name:"叶子节点213"},
         { id:214, pId:21, name:"叶子节点214"},
         { id:22, pId:2, name:"父节点22 - 折叠"},
         { id:221, pId:22, name:"叶子节点221"},
         { id:222, pId:22, name:"叶子节点222"},
         { id:223, pId:22, name:"叶子节点223"},
         { id:224, pId:22, name:"叶子节点224"},
         { id:23, pId:2, name:"父节点23 - 折叠"},
         { id:231, pId:23, name:"叶子节点231"},
         { id:232, pId:23, name:"叶子节点232"},
         { id:233, pId:23, name:"叶子节点233"},
         { id:234, pId:23, name:"叶子节点234"},
         { id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true}*/
        menuType: 2, //添加类型，默认为菜单
        pName: '',   //父级目录名称
        id: '',      //父级目录id
        newName: '', //新添加的名称
        prompt2: '', //名称是否错误的提示--默认隐藏
        flag: false, //用来控制是否可以提交
        menuPath: '',//路径
        biaoji: '',   //排序标记
        descContent: '',//描述
        nodes: [],    //选择好目录后的值
        prompt1: '',  //点击确定验证非空的提示

        menuId: '',      //修改记录的id
        menuType_: '',   //修改类型
        editName: '',    //修改名称
        editPath: '',    //修改路径
        biaoji_: '',      //排序标记
        editDescContent: '',//修改描述
        store_search:'',
    },
    created(){
        this.getlayer();
    },
    methods: {
        //加载layer
        getlayer(){
            layui.use('layer', function () {
                let layer = layui.layer;
            });
        },
        // 获取列表
        getdatas: function (num,flag) {
            let _this = this;
            if(flag){
                _this.search_input=_this.store_search;
            }
            let data = {
                pageIndex: num,             //请求哪一页的数据
                pageNum: parseInt(this.page_num),     //每页的条数
                pageSize: 10,               //后端要求写10
                menuName: this.search_input.trim()  //搜索框内容
            };
            ////console.log(data);
            let obj = {
                type: "get",
                data: data,
                dataType: 'json',
                url: '/sysMenu/queryLike',
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
            base.sendRequest(obj);
        },
        //点击修改状态
        changeType: function (menuId, state, index) {
            let _this = this;
            if (state == 1) {
                state = 0;
            } else {
                state = 1;
            }
            let data = {
                menuId: menuId,
                state: state
            };
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: "/sysMenu/isStartMenu",
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].state = state;
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
        //点击修改信息
        set: function (menuId) {
            this.editPath = '';
            this.editDescContent = '';
            this.menuId = menuId;
            let _this = this;
            let obj = {
                type: 'get',
                data: {menuId: menuId},
                dataType: 'json',
                url: '/sysMenu/querySysMenuById',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.menuType_ = data.body.menuType;
                        _this.editName = data.body.menuName;
                        _this.editPath = data.body.menuPath;
                        _this.biaoji_ = data.body.flag;
                        _thiseditDescContent = data.body.descContent;
                        $('.popEdit').show();
                        $('.zhezhao').show();
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击修改信息框里面的确定按钮
        confirmEdit: function () {
            if (this.editName.trim() == '') {
                layer.msg('名称不能为空');
                return;
            }
            if (this.menuType_ == 2) {
                if (this.editPath.trim() == '') {
                    layer.msg('路径不能为空');
                    return;
                }
            } else {
                this.editPath = '';
            }
            let data = {
                menuId: this.menuId,
                menuType: this.menuType_,
                menuName: this.editName,
                menuPath: this.editPath,
                flag: this.biaoji_,
                descContent: this.editDescContent
            };
            let obj = {
                type: "post",
                data: data,
                dataType: 'json',
                url: '/sysMenu/updateSysMenu',
                success: function (data) {
                    ////console.log(data);
                    layer.msg(data.msg);
                    setTimeout(function () {
                        $('.popEdit').hide();
                        $('.zhezhao').hide();
                        window.location.reload();
                    }, 1000)
                },
                error: function (msg) {
                    //console.log(msg);
                    $('.popEdit').hide();
                    $('.zhezhao').hide();
                }
            };
            base.sendRequest(obj);
        },
        //点击修改信息里面的取消按钮
        escEdit: function () {
            $('.popEdit').hide();
            $('.zhezhao').hide();
        },
        //点击搜索执行
        search: function () {
            this.getdatas(1,true);
            /*let _this = this;
            let obj = {
                type: 'get',
                data: {
                    pageIndex: 1,
                    pageNum: parseInt(this.page_num),
                    pageSize: 10,
                    menuName: this.search_input.trim()
                },
                dataType: 'json',
                url: '/sysMenu/queryLike',
                success: function (data) {
                    ////console.log(data);
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
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);*/
        },
        //点击添加按钮执行
        popadd: function () {
            $('#pName').val('');
            this.prompt1 = '';
            this.prompt2 = '';
            this.newName = '';
            this.menuPath = '';
            this.descContent = '';
            this.biaoji = '';
            $('.popAdd').show();
            $('.zhezhao').show();
        },
        //点击添加框确定按钮执行
        confirm: function () {
            if (this.nodes.length == 0) {
                layer.msg('请选择父级目录');
                return;
            }
            if (this.newName.trim() == '') {
                layer.msg('请输入标题');
                return;
            }
            if (this.menuType == 2) {
                if (this.menuPath.trim() == '') {
                    layer.msg('请输入页面路径');
                    return;
                }
            } else if (this.menuType == 1) {
                this.menuPath = '';
            }
            if (this.biaoji.trim() == '') {
                layer.msg('请输入排序标记');
                return;
            }
            if (this.flag == false) {
                layer.msg('标题重名');
                return;
            }
            let data = {
                menuParentId: this.id,        //父id
                menuName: this.newName,       //新名称
                menuType: this.menuType,      //要添加的类型
                descContent: this.descContent,//描述
                menuPath: this.menuPath,      //新添加的路径
                flag: this.biaoji              //排序标记
            };
            ////console.log(data);
            let obj = {
                type: "post",
                data: data,
                url: '/sysMenu/addSysMenu',
                success: function (data) {
                    ////console.log(data);
                    layer.msg(data.msg);
                    setTimeout(function () {
                        $('.popAdd').hide();
                        $('.zhezhao').hide();
                        window.location.reload();
                    }, 1000)
                },
                error: function (msg) {
                    //console.log(msg);
                    $('.popAdd').hide();
                    $('.zhezhao').hide();
                }
            };
            base.sendRequest(obj);
        },
        //点击添加框取消按钮
        esc: function () {
            $('.popAdd').hide();
            $('.zhezhao').hide();
        },
        //点击弹出目录按钮
        pop: function () {
            let _this = this;
            let obj = {
                type: "get",
                url: "/sysMenu/sysMenuSubList",
                data: {},
                dataType: "json",
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.zNodes = data.body;
                        $.fn.zTree.init($("#treeDemo"), _this.setting, _this.zNodes);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
            $('.menuList').show();
        },
        //弹出选择目录后点击确定执行
        confirmMenu: function () {
            let treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            let nodes = treeObj.getCheckedNodes(true);
            ////console.log(nodes);
            if (nodes.length == 0) {
                alert('请选择一个');
                return;
            }
            this.nodes = nodes;
            this.id = nodes[0].id;
            $('#pName').val(nodes[0].name);
            $('.menuList').hide();
        },
        //弹出选择目录后点击取消执行
        escMenu: function () {
            $('.menuList').hide();
        },
        //添加菜单框里标题离开事件
        checkName: function () {
            if (this.newName.trim() == '') {
                this.prompt2 = '标题不能为空';
                $('#prompt2').show();
                return;
            }
            let _this = this;
            let obj = {
                type: 'get',
                data: {menuName: this.newName},
                dataType: 'json',
                url: '/sysMenu/queryByMenuName',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.flag = true;
                        _this.prompt2 = data.msg;
                        $('#prompt2').css('color', 'green');
                        $('#prompt2').show();
                    } else {
                        _this.flag = false;
                        _this.prompt2 = data.msg;
                        $('#prompt2').css('color', 'darkred');
                        $('#prompt2').show();
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
        //监听menuType的值
        menuType: function () {
            if (this.menuType == 1) {
                this.menuPath = '';
                $('#path').attr('disabled', true);
            } else {
                $('#path').attr('disabled', false);
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