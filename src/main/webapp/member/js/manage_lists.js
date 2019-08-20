/**
 * Created by ASUS on 2017/9/12.
 */
let app = new Vue({
    el: '#app',
    data: {
        datas: [],//用户列表
        lists: [], //权限列表
        //分页
        page_num: 50, //分页中每页显示多少条数据
        total: '',   //信息总条数
        //添加角色
        name: '',      //添加角色用户名
        description:'',//描述
        prompt2: '',   //添加角色提示
        flag: false,   //控制角色用户名是否可以提交


        setting: {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        },
        zNodes: [],
        code: '',
        roleId: '',


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
        //获取角色列表
        getdatas: function (num) {
            let data = {
                pageIndex: num,
                pageNum: parseInt(this.page_num),
                pageSize: 10
            };
            let _this = this;
            let options = {
                type: 'get',
                url: '/sysRole/querySysRoleList',
                data: data,
                dataType: 'json',
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
        //点击更改角色权限
        //descContent菜单描述，isPublish是否授权(1授权0未授权)。menuId菜单ID。menuLevels菜单层级。
        // menuName菜单名称。menuParentId菜单父ID。menuPath菜单链接。menuType菜单类型：1目录，2子菜单。state状态：1启用，2禁用
        //点击取消隐藏
        esc: function () {
            $('.zhezhao').hide();
            $('.popAuthority').hide();
        },
        //点击确定权限按钮
        confirmRole: function () {
            let treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            let nodes = treeObj.getCheckedNodes(true);
            ////console.log(nodes);
            let ids = "";
            let i = 1;
            for (let index in nodes) {
                if (i == nodes.length)
                    ids = ids + nodes[index].id;
                else
                    ids = ids + nodes[index].id + ",";
                i++;
            }
            let data = {
                roleId: this.roleId,
                menuIds: ids
            };
            let _this = this;
            let options = {
                type: 'post',
                url: '/authorization/batchAuthorizationMenu',
                data: data,
                dataType: 'json',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        layer.msg(data.msg);
                    } else {
                        layer.msg(data.msg);
                    }
                    setTimeout(function () {
                        layer.close(layer.index);
                        $('.zhezhao').hide();
                        $('.popAuthority').hide();
                        parent.location.reload();
                    }, 1000)
                },
                error: function (msg) {
                    //console.log(msg);
                    $('.zhezhao').hide();
                    $('.popAuthority').hide();
                }
            };
            base.sendRequest(options);
        },
        //点击弹出权限菜单
        updateRole: function (roleId, state) {
            if (state == 0) {
                layer.msg('请启用后再设置');
                return;
            }
            this.roleId = roleId;
            let _this = this;
            let options = {
                type: "get",
                url: "/authorization/queryAuthorizationMenu",
                data: {
                    roleId: roleId
                },
                dataType: "json",
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.zNodes = data.body;
                        $.fn.zTree.init($("#treeDemo"), _this.setting, _this.zNodes);
                        _this.setCheck();
                        $("#py").bind("change", _this.setCheck);
                        $("#sy").bind("change", _this.setCheck);
                        $("#pn").bind("change", _this.setCheck);
                        $("#sn").bind("change", _this.setCheck);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(options);
            $('.zhezhao').show();
            $('.popAuthority').show();
        },
        showCode: function (str) {
            if (!this.code) code = $("#code");
            //this.code.empty();
            //this.code.append("<li>" + str + "</li>");
        },
        setCheck: function () {
            let zTree = $.fn.zTree.getZTreeObj("treeDemo");
            // py = $("#py").attr("checked")? "p":"",
            // sy = $("#sy").attr("checked")? "s":"",
            // pn = $("#pn").attr("checked")? "p":"",
            // sn = $("#sn").attr("checked")? "s":"",
            type = {"Y": 'ps', "N": 'ps'};
            zTree.setting.check.chkboxType = type;
            this.showCode(this.setting.check.chkboxType = {"Y": +type.Y + ',' + "N:" + type.N})
        },


        //点击改变角色状态
        changeType: function (roleId, state, index) {
            let _this = this;
            ////console.log(roleId + ',' + state);
            if (state == 1) {
                state = 0;
            } else {
                state = 1;
            }
            let obj = {
                type: 'post',
                data: {
                    roleId: roleId,
                    state: state
                },
                dataType: 'json',
                url: "/sysRole/isStartRole",
                success: function (data) {
                    if (data.code == 200) {
                        _this.datas[index].state = state;
                        layer.msg(data.msg);
                    } else {
                        layer.msg(data.msg);
                    }
                    // //console.log(data);
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击弹出添加角色弹出框
        imt: function () {
            this.name = ''; //添加角色用户名
            this.prompt2 = ''; //添加角色提示
            let _this = this;
            layer.open({
                title: '添加角色',
                type: 1,
                content: $('.popAdd'),
                area:['30%','50%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.name.trim() === '') {
                        layer.msg('角色名不能为空');
                        return;
                    }
                    if (_this.flag == false) {
                        layer.msg('角色名已存在');
                        return;
                    }
                    if(_this.description.trim()==''){
                        layer.msg('描述不能为空');
                        return;
                    }
                    let obj = {
                        type: 'post',
                        data: {
                            roleName: _this.name,
                            roleDesc:_this.description
                        },
                        dataType: 'json',
                        url: '/sysRole/addRole',
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
        //失焦验证用户名是否存在
        checkUserName: function () {
            if (this.name.trim().length<2||this.name.trim().length>10||this.name.trim() === '') {
                layer.msg('格式错误!中文2-10位');
                return;
            }
            let a = /[\u4e00-\u9fa5]/;
            let b = this.name.match(a);
            if(!b){
                layer.msg('格式错误，中文2-10位!');
                return;
            }
            let _this = this;
            let obj = {
                type: 'get',
                data: {roleName: this.name},
                dataType: 'json',
                url: '/sysRole/queryRoleByRoleName',
                success: function (data) {
                    if (data.code == 200) {
                        _this.prompt2 = data.msg;
                        $('#prompt2').css('color', 'green');
                        $('#prompt2').show();
                        _this.flag = true;
                    } else {
                        _this.prompt2 = data.msg;
                        $('#prompt2').css('color', 'darkred');
                        $('#prompt2').show();
                        _this.flag = false;
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
        },
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